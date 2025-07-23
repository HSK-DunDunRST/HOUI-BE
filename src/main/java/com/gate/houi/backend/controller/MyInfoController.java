package com.gate.houi.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gate.houi.backend.dto.account.AccountProfileResponseDTO;
import com.gate.houi.backend.service.AccountService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()") // 모든 엔드포인트는 인증 필요
public class MyInfoController {

    @Autowired
    private final AccountService accountService;

    /**
     * 현재 로그인한 학생의 기본 프로필 조회
     * JWT 토큰을 통해 인증된 사용자의 기본 정보를 반환
     */
    @GetMapping
    public ResponseEntity<AccountProfileResponseDTO> getMyProfile(@AuthenticationPrincipal UserDetails userDetails) {
        // UserDetails의 username은 JWT 토큰에서 추출한 구글 사용자 ID (oauthId)
        String oauthId = userDetails.getUsername();
        //* Debug 전용 출력문 (UserDetails에서 넘어오는 구글 사용자 ID 확인) */
        System.out.println("Google User ID from UserDetails: " + oauthId);
        AccountProfileResponseDTO studentProfile = accountService.getAccountProfile(oauthId);
        
        return ResponseEntity.ok(studentProfile);
    }

    /**
     * 현재 로그인한 학생의 상세 프로필 조회 (통계 정보 포함)
     * 기본 정보 + 진료 접수 횟수, 이용 내역 수 등의 통계 정보를 포함
     */
    @GetMapping("/edit")
    public ResponseEntity<AccountProfileResponseDTO> getMyDetailedProfile(@AuthenticationPrincipal UserDetails userDetails) {
        String oauthId = userDetails.getUsername();
        AccountProfileResponseDTO detailedProfile = accountService.getDetailedAccountProfile(oauthId);
        
        return ResponseEntity.ok(detailedProfile);
    }
}
