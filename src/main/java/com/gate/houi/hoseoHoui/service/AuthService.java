package com.gate.houi.hoseoHoui.service;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Optional;

import org.checkerframework.checker.units.qual.s;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gate.houi.hoseoHoui.domain.entity.StudentEntity;
import com.gate.houi.hoseoHoui.dto.auth.GoogleTokenRequest;
import com.gate.houi.hoseoHoui.dto.auth.JwtTokenResponse;
import com.gate.houi.hoseoHoui.repository.StudentRepository;
import com.gate.houi.hoseoHoui.security.JwtTokenProvider;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final StudentRepository studentRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final GoogleIdTokenVerifier googleIdTokenVerifier;

    @Transactional
    public JwtTokenResponse loginWithGoogle(GoogleTokenRequest googleTokenRequest) {
        //* 구글 토큰 확인 디버그용 출력 */
        System.out.println("구글 ID 토큰: " + googleTokenRequest.getToken());
        try { GoogleIdToken googleIdToken = googleIdTokenVerifier.verify(googleTokenRequest.getToken());
            if (googleIdToken == null) {
                throw new RuntimeException("구글 인증 실패: 유효하지 않은 토큰");
            }
            //* 구글 인증 성공시 디버그용 출력 */
            System.out.println("구글 인증 성공");

            // 구글 ID 토큰에서 사용자 정보 추출
            GoogleIdToken.Payload payload = googleIdToken.getPayload();
            String studentName = payload.get("name").toString();
            String studentEmail = payload.getEmail();
            String studentId = studentEmail.split("@")[0];

            //* 구글 인증 성공 후 학생 정보 GET */
            System.out.println("학생 ID: " + studentId);
            System.out.println("학생 이름: " + studentName);
            System.out.println("학생 이메일: " + studentEmail);

            // 학생 정보를 데이터베이스에서 찾거나 새로 생성
            Optional<StudentEntity> existingStudent = studentRepository.findByOauthId(googleTokenRequest.getToken());
            if (existingStudent.isPresent()) {
                // 이미 존재하는 학생 정보가 있다면 해당 엔티티를 사용
                System.out.println("기존 학생 정보 사용: " + existingStudent.get());
                return generateTokenResponse(existingStudent.get());
            } else {
                StudentEntity studentEntity = studentRepository.findByOauthId(googleTokenRequest.getToken())
                        .orElseGet(() -> studentRepository.save(StudentEntity.builder()
                                .oauthId(googleTokenRequest.getToken())
                                .oauthProvider(StudentEntity.Provider.google)
                                .studentId(studentId)
                                .stduentName("테스트")
                                .studentEmail(studentEmail)
                                .build()));
    
                return generateTokenResponse(studentEntity);
            }


        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException("구글 인증 실패: " + e.getMessage(), e);
        }
    }

    private JwtTokenResponse generateTokenResponse(StudentEntity studentEntity) {
        String accessToken = jwtTokenProvider.generateAccessToken(studentEntity.getId().toString());
        String refreshToken = jwtTokenProvider.generateRefreshToken(studentEntity.getId().toString());

        return JwtTokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}