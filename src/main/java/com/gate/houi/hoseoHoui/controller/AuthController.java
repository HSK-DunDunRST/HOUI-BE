package com.gate.houi.hoseoHoui.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gate.houi.hoseoHoui.dto.auth.OAuthLoginResponse;
import com.gate.houi.hoseoHoui.dto.auth.OAuthRefreshTokenRequest;
import com.gate.houi.hoseoHoui.service.StudentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final StudentService studentService;

    @PostMapping("/refresh")
    public ResponseEntity<OAuthLoginResponse> refreshToken(@RequestBody OAuthRefreshTokenRequest request) {
        return ResponseEntity.ok(studentService.refreshToken(request.getToken()));
    }
}