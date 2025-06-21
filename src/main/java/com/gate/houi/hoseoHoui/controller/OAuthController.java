package com.gate.houi.hoseoHoui.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gate.houi.hoseoHoui.dto.auth.OAuthLoginResponse;
import com.gate.houi.hoseoHoui.exception.UserNotFoundException;
import com.gate.houi.hoseoHoui.security.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class OAuthController {

    @Autowired
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 현재 인증된 사용자의 정보와 JWT 토큰을 반환합니다.
     * 응답에는 accessToken, refreshToken과 사용자 정보(학번, 이름, 이메일)가 포함됩니다.
     * 
     * @param oauth2User 현재 인증된 OAuth2 사용자
     * @return 사용자 정보와 토큰이 포함된 OAuthLoginResponse
     */
    @GetMapping("/user-info")
    public ResponseEntity<OAuthLoginResponse> getUserInfo(@AuthenticationPrincipal OAuth2User oauth2User) {
        if (oauth2User == null) {
            throw new UserNotFoundException("사용자 정보가 없습니다. 로그인 후 다시 시도해주세요.");
        }
        
        // 기본 정보 추출
        var attributes = oauth2User.getAttributes();
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        
        // 이메일에서 학번 추출 (이메일 형식이 studentId@vision.hoseo.edu 형태로 가정)
        String studentId = null;
        if (email != null && email.contains("@")) {
            studentId = email.split("@")[0];
        }
        
        // JWT 토큰 생성
        String accessToken = jwtTokenProvider.generateAccessToken(studentId);
        String refreshToken = jwtTokenProvider.generateRefreshToken(studentId);
        
        // 응답 객체 구성
        OAuthLoginResponse.UserInfo userInfo = OAuthLoginResponse.UserInfo.builder()
                .studentId(studentId)
                .studentName(name)
                .studentEmail(email)
                .build();
        
        OAuthLoginResponse response = OAuthLoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(userInfo)
                .build();
        
        return ResponseEntity.ok(response);
    }
}