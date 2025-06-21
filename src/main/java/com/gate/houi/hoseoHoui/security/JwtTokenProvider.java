package com.gate.houi.hoseoHoui.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.gate.houi.hoseoHoui.config.JwtConfig;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    @Autowired
    private final JwtConfig jwtConfig;

    public String generateAccessToken(String userId) {
        System.out.println("[토큰 생성] 사용자 ID: " + userId + "에 대한 액세스 토큰을 생성합니다.");
        return generateToken(userId, jwtConfig.getAccessTokenValidityInSeconds());
    }

    public String generateRefreshToken(String userId) {
        System.out.println("[토큰 생성] 사용자 ID: " + userId + "에 대한 리프레시 토큰을 생성합니다.");
        return generateToken(userId, jwtConfig.getRefreshTokenValidityInSeconds());
    }

    private String generateToken(String username, long validityInSeconds) {
        Map<String, Object> claims = new HashMap<>();
        Date now = new Date(System.currentTimeMillis());
        Date expiration = new Date(now.getTime() + validityInSeconds * 1000);
        System.out.println("[토큰 정보] 토큰 발행 시간: " + now + ", 만료 시간: " + expiration);
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        String username = extractClaim(token, Claims::getSubject);
        System.out.println("[토큰 검증] 토큰에서 추출한 사용자 ID: " + username);
        return username;
    }

    public Date extractExpiration(String token) {
        Date expiration = extractClaim(token, Claims::getExpiration);
        System.out.println("[토큰 검증] 토큰 만료 시간: " + expiration);
        return expiration;
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        System.out.println("[토큰 파싱] 토큰을 파싱합니다.");
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            System.out.println("[토큰 파싱 실패] 오류 발생: " + e.getMessage());
            throw e;
        }
    }

    private Key getSigningKey() {
        byte[] keyBytes = jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Boolean isTokenExpired(String token) {
        try {
            boolean isExpired = extractExpiration(token).before(new Date());
            System.out.println("[토큰 상태] 토큰 만료 여부: " + (isExpired ? "만료됨" : "유효함"));
            return isExpired;
        } catch (Exception e) {
            System.out.println("[토큰 상태 확인 실패] 토큰 만료 확인 중 오류 발생: " + e.getMessage());
            return true;
        }
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        System.out.println("[토큰 검증] 사용자 정보와 토큰을 검증합니다.");
        final String username = extractUsername(token);
        boolean isValid = (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        System.out.println("[토큰 검증 결과] 사용자 일치: " + username.equals(userDetails.getUsername()) + ", 토큰 유효: " + !isTokenExpired(token));
        return isValid;
    }

    // validateToken 메소드 추가 (오버로드)
    public Boolean validateToken(String token) {
        try {
            System.out.println("[토큰 검증] 토큰 유효성을 검사합니다.");
            boolean isValid = !isTokenExpired(token);
            System.out.println("[토큰 검증 결과] 토큰 유효 여부: " + isValid);
            return isValid;
        } catch (Exception e) {
            System.out.println("[토큰 검증 실패] 오류 발생: " + e.getMessage());
            return false;
        }
    }

    // getUserIdFromToken 메소드 추가
    public String getUserIdFromToken(String token) {
        String userId = extractUsername(token);
        System.out.println("[토큰 정보] 토큰에서 추출한 사용자 ID: " + userId);
        return userId;
    }
}

