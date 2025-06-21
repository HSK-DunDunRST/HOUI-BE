package com.gate.houi.hoseoHoui.service;

import lombok.RequiredArgsConstructor;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gate.houi.hoseoHoui.domain.entity.StudentEntity;
import com.gate.houi.hoseoHoui.dto.auth.OAuthRefreshTokenRequest;
import com.gate.houi.hoseoHoui.dto.auth.OAuthLoginResponse;
import com.gate.houi.hoseoHoui.repository.StudentRepository;
import com.gate.houi.hoseoHoui.security.JwtTokenProvider;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthService extends DefaultOAuth2UserService{

    private final StudentRepository studentRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public OAuthLoginResponse loginWithGoogle(OAuthRefreshTokenRequest request) {
        // Google API를 통해 ID 토큰 검증 및 사용자 정보 가져오기
        GoogleIdToken idToken = verifyGoogleIdToken(request.getToken());
        GoogleIdToken.Payload payload = idToken.getPayload();
        
        // Google OAuth에서 사용자 정보 추출
        String oauthId = payload.getSubject(); // Google에서 제공하는 고유 ID
        String email = payload.getEmail();
        String name = (String) payload.get("name");
        
        StudentEntity studentEntity = studentRepository.findByOauthId(oauthId)
                                    .orElseGet(() -> studentRepository.save(StudentEntity.builder()
                                    .oauthId(oauthId)
                                    .oauthProvider(StudentEntity.Provider.google)
                                    .stduentName(name)
                                    .studentEmail(email)
                                    .build()));

        return generateTokenResponse(studentEntity);
    }

    private OAuthLoginResponse generateTokenResponse(StudentEntity studentEntity) {
        String accessToken = jwtTokenProvider.generateAccessToken(studentEntity.getId().toString());
        String refreshToken = jwtTokenProvider.generateRefreshToken(studentEntity.getId().toString());

        return OAuthLoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(OAuthLoginResponse.UserInfo.builder()
                        .studentId(studentEntity.getStudentId())
                        .studentName(studentEntity.getStduentName())
                        .studentEmail(studentEntity.getStudentEmail())
                        .build())
                .build();
    }

    // Google ID 토큰 검증
    private GoogleIdToken verifyGoogleIdToken(String idTokenString) {
        try {
            NetHttpTransport transport = new NetHttpTransport();
            JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
            
            // Google API 클라이언트 ID 설정
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList("YOUR_GOOGLE_CLIENT_ID")) // application.yml에서 가져오는 것이 좋음
                .build();
                
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken == null) {
                throw new RuntimeException("Google ID 토큰 검증 실패");
            }
            return idToken;
        } catch (Exception e) {
            throw new RuntimeException("Google ID 토큰 검증 중 오류 발생: " + e.getMessage(), e);
        }
    }

}