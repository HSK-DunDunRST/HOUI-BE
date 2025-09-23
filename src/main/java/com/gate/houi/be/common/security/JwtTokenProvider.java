package com.gate.houi.be.common.security;

import com.gate.houi.be.apiPayload.code.exception.BaseException;
import com.gate.houi.be.apiPayload.code.status.ErrorType;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;
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

    private Key secretKey;

    private final CustomUserDetailsService userDetailsService;

    @Qualifier("jwtRedisTemplate")
    private final RedisTemplate<String, Object> redisTemplate;

    @PostConstruct
    protected void init() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) {
            throw new IllegalArgumentException("JWT 비밀키는 최소 256비트(32바이트) 이상이어야 합니다");
        }
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    // ===================== 토큰 생성 =====================

    public String generateAccessToken(String studentUuid) {
        return generateToken(studentUuid, accessTokenValidityInSeconds, TOKEN_TYPE_ACCESS);
    }

    public String generateRefreshToken(String studentUuid) {
        String refreshToken = generateToken(studentUuid, refreshTokenValidityInSeconds, TOKEN_TYPE_REFRESH);

        // Redis 저장 (만료시간과 동일하게 설정)
        redisTemplate.opsForValue().set(
                "RT:" + studentUuid,
                refreshToken,
                refreshTokenValidityInSeconds,
                TimeUnit.SECONDS
        );
        return refreshToken;
    }

    private String generateToken(String studentUuid, long validityInSeconds, String tokenType) {
        long currentTimeMillis = System.currentTimeMillis();
        Date now = new Date(currentTimeMillis);
        Date expiration = new Date(currentTimeMillis + validityInSeconds * 1000);

        return Jwts.builder()
                .claim(CLAIM_TOKEN_TYPE, tokenType)
                .claim(CLAIM_ISSUER, ISSUER_VALUE)
                .setSubject(studentUuid)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .setIssuer(ISSUER_VALUE)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // ===================== 토큰 검증 =====================

    public void assertValidTokenOrThrow(String token) {
        if (!StringUtils.hasText(token)) {
            throw new BaseException(ErrorType.TOKEN_EMPTY);
        }
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .setAllowedClockSkewSeconds(CLOCK_SKEW_ALLOWANCE)
                    .requireIssuer(ISSUER_VALUE)
                    .build()
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw new BaseException(ErrorType.TOKEN_EXPIRED);
        } catch (MalformedJwtException | SignatureException | UnsupportedJwtException e) {
            throw new BaseException(ErrorType.TOKEN_INVALID);
        } catch (IllegalArgumentException e) {
            throw new BaseException(ErrorType.TOKEN_EMPTY);
        }

        if (isTokenInvalidated(token)) {
            throw new BaseException(ErrorType.TOKEN_INVALID);
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return !isTokenInvalidated(token);
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("JWT validation failed: {}", e.getMessage());
            return false;
        }
    }

    public boolean validateRefreshToken(String token, String studentUuid) {
        String storedToken = getStoredRefreshToken(studentUuid);
        return token.equals(storedToken) && validateToken(token);
    }

    // ===================== Redis 관련 =====================

    public String getStoredRefreshToken(String studentUuid) {
        return (String) redisTemplate.opsForValue().get("RT:" + studentUuid);
    }

    public void deleteRefreshToken(String studentUuid) {
        redisTemplate.delete("RT:" + studentUuid);
    }

    public void invalidateToken(String token) {
        long expiration = getTokenExpiration(token);
        redisTemplate.opsForValue().set("BL:" + token, "logout", expiration, TimeUnit.MILLISECONDS);
    }

    public boolean isTokenInvalidated(String token) {
        return redisTemplate.hasKey("BL:" + token);
    }

    private long getTokenExpiration(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return claims.getExpiration().getTime() - System.currentTimeMillis();
        } catch (ExpiredJwtException e) {
            return 0;
        }
    }

    // ===================== Claim & ID 추출 =====================

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractTokenType(String token) {
        return extractClaim(token, claims -> claims.get(CLAIM_TOKEN_TYPE, String.class));
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .setAllowedClockSkewSeconds(CLOCK_SKEW_ALLOWANCE)
                .requireIssuer(ISSUER_VALUE)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // ===================== Authentication 생성 =====================

    public Authentication getAuthentication(String token) {
        String username = extractUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public boolean processTokenAndSetAuthContext(String token) {
        if (token != null && validateToken(token)) {
            Authentication authentication = getAuthentication(token);
            org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
            return true;
        }
        return false;
    }

    public void processTokenAndSetAuthContextOrThrow(String token) {
        assertValidTokenOrThrow(token);
        Authentication authentication = getAuthentication(token);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    // ===================== 토큰 추출 =====================

    public String resolveAccessToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String token = request.getHeader("Authorization");
        return (token != null && token.startsWith("Bearer ")) ? token.substring(7) : token;
    }

    public String resolveRefreshToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String token = request.getHeader("Refresh-Token");
        if (token != null && token.startsWith("Bearer ")) token = token.substring(7);
        return StringUtils.hasText(token) ? token : null;
    }

    // ===================== 로그아웃 처리 =====================

    public void handleLogout(String accessToken, String refreshToken) {
        if (accessToken != null) {
            invalidateToken(accessToken);
        }
        if (refreshToken != null) {
            String studentUuid = extractUsername(refreshToken);
            deleteRefreshToken(studentUuid);
        }
    }
}