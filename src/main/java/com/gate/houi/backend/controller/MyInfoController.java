package com.gate.houi.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gate.houi.backend.dto.student.StudentProfileResponseDTO;
import com.gate.houi.backend.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()") // 모든 엔드포인트는 인증 필요
public class MyInfoController {

    @Autowired
    private final UserService userService;

    /**
     * 현재 로그인한 학생의 기본 프로필 조회
     * JWT 토큰을 통해 인증된 사용자의 기본 정보를 반환
     */
    @GetMapping("/info")
    public ResponseEntity<StudentProfileResponseDTO> getMyProfile(@AuthenticationPrincipal UserDetails userDetails) {
        // UserDetails의 username은 JWT 토큰에서 추출한 OAuth ID
        String oauthId = userDetails.getUsername();
        StudentProfileResponseDTO studentProfile = userService.getAccountProfile(oauthId);

        return ResponseEntity.ok(studentProfile);
    }
}
