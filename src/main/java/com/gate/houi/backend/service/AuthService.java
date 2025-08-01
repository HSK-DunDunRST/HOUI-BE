package com.gate.houi.backend.service;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gate.houi.backend.data.entityType.StudentEntity;
import com.gate.houi.backend.data.entityType.RefreshTokenEntity;
import com.gate.houi.backend.dto.auth.GoogleTokenRequestDTO;
import com.gate.houi.backend.dto.auth.JwtTokenResponseDTO;
import com.gate.houi.backend.repository.StudentRepository;
import com.gate.houi.backend.security.JwtTokenProvider;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final StudentRepository studentRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final GoogleIdTokenVerifier googleIdTokenVerifier;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public JwtTokenResponseDTO loginWithGoogle(GoogleTokenRequestDTO googleTokenRequest) {
        //* 구글 토큰 확인 디버그용 출력 */
        System.out.println("구글 ID 토큰: " + googleTokenRequest.getToken());
        try { GoogleIdToken googleIdToken = googleIdTokenVerifier.verify(googleTokenRequest.getToken());
            if (googleIdToken == null) {
                throw new RuntimeException("구글 인증 실패: 유효하지 않은 토큰");
            }
            //* 구글 인증 성공시 디버그용 출력 */
            System.out.println("구글 인증 성공");

            // 구글 ID 토큰에서 사용자 정보 추출
            Payload payload = googleIdToken.getPayload();
            String googleUserId = payload.getSubject(); // 구글의 고유 사용자 ID 사용
            String userName = payload.get("name").toString();
            String userEmail = payload.getEmail();

            // 이메일에서 학번 추출 (예: 20201234@hoseo.edu)
            String studentId = userEmail.split("@")[0];

            //* 구글 인증 성공 후 학생 정보 GET */
            System.out.println("구글 사용자 ID: " + googleUserId);
            System.out.println("학생 ID: " + studentId);
            System.out.println("학생 이름: " + userName);
            System.out.println("학생 이메일: " + userEmail);

            // 학생 정보를 데이터베이스에서 찾거나 새로 생성
            Optional<StudentEntity> existingStudent = studentRepository.findByOauthId(payload.getSubject());
            if (existingStudent.isPresent()) {
                // 이미 존재하는 학생 정보가 있다면 구글 로그인 시간 재갱신
                StudentEntity account = existingStudent.get();
                account.getUpdatedAt(); // 업데이트 시간 갱신
                studentRepository.save(account);

                //* 구글 인증 성공 후 DB와 비교 테스트 출력 */
                System.out.println("기존 학생 정보 사용 및 oauthId 업데이트: " + account.getStudentId()+ " : " + account.getStudentName());

                return generateTokenResponse(account);
            } else {
                System.out.println("기존 학생 정보가 없어 정보를 갱신합니다");
                StudentEntity userEntity = studentRepository.findByOauthId(googleUserId)
                        .orElseGet(() -> studentRepository.save(StudentEntity.builder()
                                .oauthId(googleUserId) // 구글 토큰 대신 구글 사용자 ID 사용
                                .oauthProvider(StudentEntity.Provider.google)
                                .studentId(studentId)
                                .studentName(userName)
                                .build()));
    
                return generateTokenResponse(userEntity);
            }

        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException("구글 인증에 실패하였습니다.");
        }
    }

    private JwtTokenResponseDTO generateTokenResponse(StudentEntity studentEntity) {
        // 보안을 위해 OAuth ID를 JWT 토큰에 사용 (일관성 있는 식별자)
        String accessToken =  jwtTokenProvider.generateAccessToken(studentEntity.getOauthId());
        String refreshToken = jwtTokenProvider.generateRefreshToken(studentEntity.getOauthId());

        // RefreshToken을 데이터베이스에 저장
        refreshTokenService.saveRefreshToken(studentEntity, refreshToken);

        return JwtTokenResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public void logout(String refreshToken) {
        // 리프레시 토큰으로 학생 정보 찾기
        RefreshTokenEntity refreshTokenEntity = refreshTokenService.findByRefreshToken(refreshToken);
        if (refreshTokenEntity != null) {
            // UUID로 StudentEntity 조회
            StudentEntity studentEntity = studentRepository.findByStudentUuid(refreshTokenEntity.getStudentUuid())
                    .orElse(null);
            if (studentEntity != null) {
                // 리프레시 토큰 삭제
                refreshTokenService.deleteRefreshToken(studentEntity);
            }
        }
    }
}