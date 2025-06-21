package com.gate.houi.hoseoHoui.security;

import java.io.IOException;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.gate.houi.hoseoHoui.domain.entity.StudentEntity;
import com.gate.houi.hoseoHoui.service.StudentService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final StudentService studentService;
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        
        System.out.println("[OAuth2 성공 핸들러] 구글 인증 성공 처리 시작");
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        
        // OAuth2User로부터 학생 정보 추출
        String email = oAuth2User.getAttribute("email");
        String studentId = null;
        if (email != null && email.contains("@")) {
            studentId = email.split("@")[0];
        }
        
        // StudentEntity 조회 또는 생성
        String oauthId = oAuth2User.getName();
        String name = oAuth2User.getAttribute("name");
        
        StudentEntity student = studentService.findOrCreateGoogleStudent(email, name, oauthId);
        
        // JWT 토큰 생성
        String accessToken = jwtTokenProvider.generateAccessToken(student.getId().toString());
        String refreshToken = jwtTokenProvider.generateRefreshToken(student.getId().toString());
        
        System.out.println("[OAuth2 성공 핸들러] 토큰 생성 완료 - 사용자 ID: " + student.getId());
        
        // 리다이렉트 URL 생성 (프론트엔드로 토큰 전달)
        String redirectUrl = UriComponentsBuilder.fromUriString("/notice")
                .queryParam("accessToken", accessToken)
                .queryParam("refreshToken", refreshToken)
                .build().toUriString();
        
        System.out.println("[OAuth2 성공 핸들러] 리다이렉트 URL: " + redirectUrl);
        
        // 인증 정보를 SecurityContext에 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // 설정된 URL로 리다이렉트
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}
