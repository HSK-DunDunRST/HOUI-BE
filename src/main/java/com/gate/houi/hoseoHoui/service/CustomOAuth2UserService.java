package com.gate.houi.hoseoHoui.service;

import java.util.Collections;
import java.util.Map;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        
        // Google Login
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                                                    .getProviderDetails()
                                                    .getUserInfoEndpoint()
                                                    .getUserNameAttributeName();
        
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");
        
        // 이메일에서 학번 추출 (@hoseo.edu 이메일 형식 가정)
        String studentId = extractStudentId(email);
        
        
        // 사용자 속성 정보에 학번 추가
        Map<String, Object> customAttributes = Map.of(
            "studentId", studentId,
            "email", email,
            "name", attributes.get("name")
        );
        
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                customAttributes,
                "email"
        );
    }
    
    // 이메일에서 학번 추출
    private String extractStudentId(String email) {
        if (email != null && email.contains("@")) {
            return email.split("@")[0];
        }
        return email;
    }
    
}