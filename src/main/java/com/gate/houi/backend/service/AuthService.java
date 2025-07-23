package com.gate.houi.backend.service;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gate.houi.backend.data.entityType.AccountEntity;
import com.gate.houi.backend.data.entityType.RefreshTokenEntity;
import com.gate.houi.backend.dto.auth.GoogleTokenRequest;
import com.gate.houi.backend.dto.auth.JwtTokenResponse;
import com.gate.houi.backend.repository.AccountRepository;
import com.gate.houi.backend.security.JwtTokenProvider;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AccountRepository accountRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final GoogleIdTokenVerifier googleIdTokenVerifier;
    private final RefreshTokenService refreshTokenService;

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
            Payload payload = googleIdToken.getPayload();
            String googleUserId = payload.getSubject(); // 구글의 고유 사용자 ID 사용
            String studentName = payload.get("name").toString();
            String studentEmail = payload.getEmail();

            // 이메일에서 학번 추출 (예: 20201234@hoseo.edu)
            String studentId = studentEmail.split("@")[0];

            //* 구글 인증 성공 후 학생 정보 GET */
            System.out.println("구글 사용자 ID: " + googleUserId);
            System.out.println("학생 ID: " + studentId);
            System.out.println("학생 이름: " + studentName);
            System.out.println("학생 이메일: " + studentEmail);

            // 학생 정보를 데이터베이스에서 찾거나 새로 생성
            Optional<AccountEntity> existingStudent = accountRepository.findByAccountEmail(studentEmail);
            if (existingStudent.isPresent()) {
                // 이미 존재하는 학생 정보가 있다면 oauthId를 업데이트
                AccountEntity account = existingStudent.get();
                account.setOauthId(googleUserId); // 구글 토큰 대신 구글 사용자 ID 사용
                accountRepository.save(account);

                //* 구글 인증 성공 후 DB와 비교 테스트 출력 */
                System.out.println("기존 학생 정보 사용 및 oauthId 업데이트: " + account.getStudentId()+ " " + account.getAccountName());

                return generateTokenResponse(account);
            } else {
                System.out.println("새로운 학생 정보 생성");
                AccountEntity studentEntity = accountRepository.findByOauthId(googleUserId)
                        .orElseGet(() -> accountRepository.save(AccountEntity.builder()
                                .oauthId(googleUserId) // 구글 토큰 대신 구글 사용자 ID 사용
                                .oauthProvider(AccountEntity.Provider.google)
                                .studentId(studentId)
                                .accountName(studentName)
                                .accountEmail(studentEmail)
                                .build()));
    
                return generateTokenResponse(studentEntity);
            }

        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException("구글 인증 실패: " + e.getMessage(), e);
        }
    }

    private JwtTokenResponse generateTokenResponse(AccountEntity accountEntity) {
        // 보안을 위해 OAuth ID를 JWT 토큰에 사용 (UUID 대신)
        String accessToken = jwtTokenProvider.generateAccessToken(accountEntity.getOauthId());
        String refreshToken = jwtTokenProvider.generateRefreshToken(accountEntity.getOauthId());

        // RefreshToken을 데이터베이스에 저장
        refreshTokenService.saveRefreshToken(accountEntity, refreshToken);

        return JwtTokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public void logout(String refreshToken) {
        // 리프레시 토큰으로 학생 정보 찾기
        RefreshTokenEntity refreshTokenEntity = refreshTokenService.findByRefreshToken(refreshToken);
        if (refreshTokenEntity != null) {
            // UUID로 AccountEntity 조회
            AccountEntity accountEntity = accountRepository.findByAccountUuid(refreshTokenEntity.getAccountUuid())
                    .orElse(null);
            if (accountRepository != null) {
                // 리프레시 토큰 삭제
                refreshTokenService.deleteRefreshToken(accountEntity);
            }
        }
    }
}