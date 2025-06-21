package com.gate.houi.hoseoHoui.service;

import com.gate.houi.hoseoHoui.domain.entity.StudentEntity;
import com.gate.houi.hoseoHoui.dto.auth.OAuthLoginResponse;
import com.gate.houi.hoseoHoui.exception.AuthenticationExceptionHandler;
import com.gate.houi.hoseoHoui.exception.UserNotFoundException;
import com.gate.houi.hoseoHoui.repository.StudentRepository;
import com.gate.houi.hoseoHoui.security.JwtTokenProvider;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentService {
    @Autowired
    private final StudentRepository studentRepository;
    @Autowired
    private final GoogleIdTokenVerifier googleTokenVerifier;
    @Autowired
    private final JwtTokenProvider jwtTokenProvider;

    
    @Transactional
    public OAuthLoginResponse authenticateWithGoogle(String idToken) {
        try {
            System.out.println("[인증 시작] 구글 ID 토큰 검증을 시작합니다.");
            GoogleIdToken googleIdToken = googleTokenVerifier.verify(idToken);
            if (googleIdToken == null) {
                System.out.println("[인증 실패] 구글 ID 토큰 검증에 실패했습니다.");
                throw new AuthenticationExceptionHandler("구글 ID 토큰 검증에 실패했어요.");
            }

            System.out.println("[인증 성공] 구글 ID 토큰 검증에 성공했습니다.");
            GoogleIdToken.Payload payload = googleIdToken.getPayload();
            System.out.println("[사용자 정보] 이메일: " + payload.getEmail() + ", 이름: " + payload.get("name"));
            
            StudentEntity student = findOrCreateGoogleStudent(payload);
            System.out.println("[사용자 정보] 학생 ID: " + student.getId() + ", 학생 이름: " + student.getStduentName());

            // JWT 토큰 생성
            String accessToken = jwtTokenProvider.generateAccessToken(student.getId().toString());
            String refreshToken = jwtTokenProvider.generateRefreshToken(student.getId().toString());
            System.out.println("[토큰 생성] 액세스 토큰과 리프레시 토큰이 생성되었습니다.");

            return OAuthLoginResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .user(mapToUserInfo(student))
                    .build();
        } catch (GeneralSecurityException | IOException e) {
            System.out.println("[인증 오류] 구글 ID 토큰 검증 중 오류 발생: " + e.getMessage());
            throw new AuthenticationExceptionHandler("구글 ID 토큰 검증 중 오류가 발생했어요.");
        }
    }

    @Transactional
    protected StudentEntity findOrCreateGoogleStudent(GoogleIdToken.Payload payload) {
        String oauthId = payload.getSubject();
        String email = payload.getEmail();
        String name = (String) payload.get("name");
        String studentId = email.substring(0, email.indexOf("@"));

        System.out.println("[사용자 조회] OAuth ID: " + oauthId + "로 학생 정보를 조회합니다.");
        return studentRepository.findByOauthId(oauthId)
                .map(student -> {
                    System.out.println("[사용자 조회] 기존 학생을 찾았습니다. 학번: " + student.getStudentId());
                    return student;
                })
                .orElseGet(() -> {
                    System.out.println("[사용자 생성] 새로운 학생을 등록합니다. 학번: " + studentId);
                    return studentRepository.save(StudentEntity.builder()
                            .oauthId(oauthId)
                            .oauthProvider(StudentEntity.Provider.google)
                            .studentId(studentId)
                            .stduentName(name)
                            .build());
                });
    }

    @Transactional
    public StudentEntity findOrCreateGoogleStudent(String email, String name, String oauthId) {
        String studentId = email.substring(0, email.indexOf("@"));

        System.out.println("[사용자 조회] OAuth ID: " + oauthId + "로 학생 정보를 조회합니다.");
        return studentRepository.findByOauthId(oauthId)
                .map(student -> {
                    System.out.println("[사용자 조회] 기존 학생을 찾았습니다. 학번: " + student.getStudentId());
                    return student;
                })
                .orElseGet(() -> {
                    System.out.println("[사용자 생성] 새로운 학생을 등록합니다. 학번: " + studentId);
                    return studentRepository.save(StudentEntity.builder()
                            .oauthId(oauthId)
                            .oauthProvider(StudentEntity.Provider.google)
                            .studentId(studentId)
                            .stduentName(name)
                            .studentEmail(email)
                            .build());
                });
    }

    public StudentEntity getAuthenticatedStudent() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            System.out.println("[인증 실패] 현재 인증된 사용자가 없습니다.");
            throw new AuthenticationExceptionHandler("인증된 사용자가 아니예요.");
        }
        
        // UserDetails에서 학생 ID 추출 후 학생 조회
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String userId = userDetails.getUsername(); // JWT에서는 username에 userId를 저장했음
        System.out.println("[인증 확인] 현재 인증된 사용자 ID: " + userId);
        
        return findById(Long.parseLong(userId));
    }

    public StudentEntity findById(Long id) {
        System.out.println("[사용자 조회] ID: " + id + "로 학생 정보를 조회합니다.");
        return studentRepository.findById(id)
                .map(student -> {
                    System.out.println("[사용자 조회] 학생을 찾았습니다. 학번: " + student.getStudentId() + ", 이름: " + student.getStduentName());
                    return student;
                })
                .orElseThrow(() -> {
                    System.out.println("[사용자 조회 실패] ID: " + id + "에 해당하는 학생을 찾을 수 없습니다.");
                    return new UserNotFoundException("해당 학생을 찾을 수 없어요");
                });
    }

    public OAuthLoginResponse refreshToken(String refreshToken) {
        System.out.println("[토큰 갱신] 리프레시 토큰을 검증합니다.");
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            System.out.println("[토큰 갱신 실패] 유효하지 않은 리프레시 토큰입니다.");
            throw new AuthenticationExceptionHandler("유효하지 않은 리프레시 토큰이에요.");
        }

        String userId = jwtTokenProvider.getUserIdFromToken(refreshToken);
        System.out.println("[토큰 갱신] 사용자 ID: " + userId + "의 토큰을 새로 발급합니다.");
        String newAccessToken = jwtTokenProvider.generateAccessToken(userId);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(userId);
        System.out.println("[토큰 갱신 성공] 새 액세스 토큰과 리프레시 토큰이 생성되었습니다.");

        return OAuthLoginResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    private OAuthLoginResponse.UserInfo mapToUserInfo(StudentEntity student) {
        return OAuthLoginResponse.UserInfo.builder()
                .studentId(student.getStudentId())
                .studentName(student.getStduentName())
                .studentEmail(student.getStudentEmail())
                .build();
    }
}
