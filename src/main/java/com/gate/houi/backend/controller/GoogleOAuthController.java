package com.gate.houi.backend.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gate.houi.backend.data.enumType.ErrorType;
import com.gate.houi.backend.dto.auth.GoogleTokenRequest;
import com.gate.houi.backend.dto.auth.JwtTokenResponse;
import com.gate.houi.backend.dto.auth.RefreshTokenRequest;
import com.gate.houi.backend.service.AuthService;
import com.gate.houi.backend.service.RefreshTokenService;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class GoogleOAuthController {

    @Autowired
    private final AuthService authService;
    @Autowired
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public ResponseEntity<JwtTokenResponse> googleLogin(@RequestBody GoogleTokenRequest googleTokenRequest) {
        // 프론트엔드에서 받은 ID 토큰을 검증하고, 학생 정보를 찾거나 저장한 후 JWT 토큰과 학생 정보를 반환
        return ResponseEntity.ok(authService.loginWithGoogle(googleTokenRequest));
    }

    /**
     * 리프레시 토큰을 사용하여 새로운 액세스 토큰과 리프레시 토큰을 발급받는 엔드포인트
     * 액세스 토큰이 만료되었을 때 사용
     */
    @PostMapping("/refresh")
    public ResponseEntity<JwtTokenResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        JwtTokenResponse tokenResponse = refreshTokenService.refreshAccessToken(refreshTokenRequest.getRefreshToken());
        return ResponseEntity.ok(tokenResponse);
    }

    /**
     * 로그아웃 엔드포인트
     * 리프레시 토큰을 삭제하여 로그아웃 처리
     * Spring Security의 기본 로그아웃 대신 JWT 특성에 맞는 커스텀 로그아웃 사용
     */
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        try {
            authService.logout(refreshTokenRequest.getRefreshToken());
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "로그아웃이 완료되었습니다.");
            response.put("timestamp", Instant.now().toString());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", ErrorType.LOGOUT_FAILED.getErrorMessage());
            errorResponse.put("timestamp", Instant.now().toString());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}