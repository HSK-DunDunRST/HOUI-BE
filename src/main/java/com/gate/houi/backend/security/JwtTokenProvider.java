package com.gate.houi.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {

    private static final String TOKEN_TYPE_ACCESS = "ACCESS";
    private static final String TOKEN_TYPE_REFRESH = "REFRESH";
    private static final String CLAIM_TOKEN_TYPE = "tokenType";
    private static final String CLAIM_ISSUER = "issuer";
    private static final String ISSUER_VALUE = "HOUI";
    private static final long CLOCK_SKEW_ALLOWANCE = 60; // 60초 클럭 스큐 허용

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration.accessTokenValidityInSeconds}")
    private long accessTokenValidityInSeconds;

    @Value("${jwt.expiration.refreshTokenValidityInSeconds}")
    private long refreshTokenValidityInSeconds;

    public String generateAccessToken(String username) {
        return generateToken(username, accessTokenValidityInSeconds, TOKEN_TYPE_ACCESS);
    }

    public String generateRefreshToken(String username) {
        return generateToken(username, refreshTokenValidityInSeconds, TOKEN_TYPE_REFRESH);
    }

    private String generateToken(String username, long validityInSeconds, String tokenType) {
        // 입력 값 검증
        if (!StringUtils.hasText(username)) {
            throw new IllegalArgumentException("사용자명은 null이거나 비어있을 수 없습니다");
        }
        if (validityInSeconds <= 0) {
            throw new IllegalArgumentException("토큰 유효기간은 양수여야 합니다");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_TOKEN_TYPE, tokenType);
        claims.put(CLAIM_ISSUER, ISSUER_VALUE);
        
        long currentTimeMillis = System.currentTimeMillis();
        Date now = new Date(currentTimeMillis);
        Date expiration = new Date(currentTimeMillis + validityInSeconds * 1000);
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .setIssuer(ISSUER_VALUE) // 발급자 명시적 설정
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        try {
            return extractClaim(token, Claims::getSubject);
        } catch (JwtException e) {
            log.error("토큰에서 사용자명 추출 실패", e);
            throw new IllegalArgumentException("유효하지 않은 토큰");
        }
    }

    public Date extractExpiration(String token) {
        try {
            return extractClaim(token, Claims::getExpiration);
        } catch (JwtException e) {
            log.error("토큰에서 만료시간 추출 실패");
            throw new IllegalArgumentException("유효하지 않은 토큰");
        }
    }

    public String extractTokenType(String token) {
        try {
            return extractClaim(token, claims -> claims.get(CLAIM_TOKEN_TYPE, String.class));
        } catch (JwtException e) {
            log.error("토큰에서 토큰 타입 추출 실패");
            throw new IllegalArgumentException("유효하지 않은 토큰");
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            // 토큰 형식 검증
            if (!StringUtils.hasText(token)) {
                throw new IllegalArgumentException("토큰이 null이거나 비어있을 수 없습니다");
            }

            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .setAllowedClockSkewSeconds(CLOCK_SKEW_ALLOWANCE) // 클럭 스큐 허용
                    .requireIssuer(ISSUER_VALUE) // 발급자 검증
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.warn("토큰이 만료되었습니다");
            throw new IllegalArgumentException("토큰이 만료되었습니다");
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 토큰입니다");
            throw new IllegalArgumentException("지원되지 않는 JWT 토큰입니다");
        } catch (MalformedJwtException e) {
            log.error("잘못된 형식의 JWT 토큰입니다");
            throw new IllegalArgumentException("잘못된 형식의 JWT 토큰입니다");
        } catch (SignatureException e) {
            log.error("유효하지 않은 JWT 서명입니다");
            throw new IllegalArgumentException("유효하지 않은 JWT 서명입니다");
        } catch (IllegalArgumentException e) {
            log.error("JWT 토큰이 유효하지 않습니다");
            throw new IllegalArgumentException("JWT 토큰이 유효하지 않습니다");
        } catch (Exception e) {
            log.error("JWT 토큰 검증이 실패했습니다");
            throw new IllegalArgumentException("JWT 토큰 검증이 실패했습니다");
        }
    }

    private Key getSigningKey() {
        // 비밀키 길이 검증 (최소 256비트 = 32바이트)
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) {
            throw new IllegalArgumentException("JWT 비밀키는 최소 256비트(32바이트) 이상이어야 합니다");
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Boolean isTokenExpired(String token) {
        try {
            Date expiration = extractExpiration(token);
            return expiration.before(new Date());
        } catch (Exception e) {
            log.error("토큰 만료 확인 중 오류 발생");
            return true; // 에러가 발생하면 만료된 것으로 간주
        }
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        try {
            // 기본 검증
            if (!StringUtils.hasText(token) || userDetails == null) {
                return false;
            }

            final String username = extractUsername(token);
            final String tokenType = extractTokenType(token);
            
            // 사용자명 검증
            boolean isUsernameValid = username.equals(userDetails.getUsername());
            
            // 토큰 만료 검증
            boolean isTokenNotExpired = !isTokenExpired(token);
            
            // 토큰 타입 검증 (Access Token인지 확인)
            boolean isValidTokenType = TOKEN_TYPE_ACCESS.equals(tokenType);
            
            return isUsernameValid && isTokenNotExpired && isValidTokenType;
            
        } catch (Exception e) {
            log.error("사용자 {}에 대한 토큰 검증 실패, 오류", 
                    userDetails != null ? userDetails.getUsername() : "알 수 없음");
            return false;
        }
    }

    public Boolean validateRefreshToken(String token, String username) {
        try {
            if (!StringUtils.hasText(token) || !StringUtils.hasText(username)) {
                return false;
            }

            final String tokenUsername = extractUsername(token);
            final String tokenType = extractTokenType(token);
            
            boolean isUsernameValid = username.equals(tokenUsername);
            boolean isTokenNotExpired = !isTokenExpired(token);
            boolean isValidTokenType = TOKEN_TYPE_REFRESH.equals(tokenType);
            
            return isUsernameValid && isTokenNotExpired && isValidTokenType;
            
        } catch (Exception e) {
            log.error("사용자 {}에 대한 리프레시 토큰 검증 실패, 오류", username);
            return false;
        }
    }
}