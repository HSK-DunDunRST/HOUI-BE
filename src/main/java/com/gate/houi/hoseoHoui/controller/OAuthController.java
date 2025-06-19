package com.gate.houi.hoseoHoui.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth")
public class OAuthController {

    /**
     * 현재 인증된 사용자의 정보를 반환합니다.
     * 학번, 이름, 이메일 및 권한 정보가 포함됩니다.
     * 
     * @param oauth2User 현재 인증된 OAuth2 사용자
     * @return 사용자 정보
     */
    @GetMapping("/user-info")
    public Map<String, Object> getUserInfo(@AuthenticationPrincipal OAuth2User oauth2User) {
        if (oauth2User == null) {
            return Map.of("authenticated", false);
        }
        
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("authenticated", true);
        
        // 기본 정보 추출
        Map<String, Object> attributes = oauth2User.getAttributes();
        String email = (String) attributes.get("email");
        
        // 이메일에서 학번 추출 (이메일 형식이 studentId@vision.hoseo.edu 형태로 가정)
        String studentId = null;
        if (email != null && email.contains("@")) {
            studentId = email.split("@")[0];
        }
        
        // 사용자 정보 구성
        userInfo.put("studentId", studentId);        // 학번
        userInfo.put("name", attributes.get("name")); // 이름
        userInfo.put("email", email);                // 이메일
        
        // 권한 정보 추가
        userInfo.put("authorities", oauth2User.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
        
        return userInfo;
    }
}
