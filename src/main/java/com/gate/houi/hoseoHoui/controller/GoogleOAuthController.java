package com.gate.houi.hoseoHoui.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gate.houi.hoseoHoui.dto.auth.GoogleTokenRequest;
import com.gate.houi.hoseoHoui.dto.auth.JwtTokenResponse;
import com.gate.houi.hoseoHoui.service.AuthService;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class GoogleOAuthController {

    private final AuthService authService;

    @PostMapping("/google")
    public ResponseEntity<JwtTokenResponse> googleLogin(@RequestBody GoogleTokenRequest googleTokenRequest) {
        // 프론트엔드에서 받은 ID 토큰을 검증하고, 학생 정보를 찾거나 저장한 후 JWT 토큰과 학생 정보를 반환
        return ResponseEntity.ok(authService.loginWithGoogle(googleTokenRequest));
    }
}