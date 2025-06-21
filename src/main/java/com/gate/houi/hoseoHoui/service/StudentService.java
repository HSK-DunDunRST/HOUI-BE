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
            GoogleIdToken googleIdToken = googleTokenVerifier.verify(idToken);
            if (googleIdToken == null) {
                throw new AuthenticationExceptionHandler("구글 ID 토큰 검증에 실패했어요.");
            }

            GoogleIdToken.Payload payload = googleIdToken.getPayload();
            StudentEntity student = findOrCreateGoogleStudent(payload);

            // JWT 토큰 생성
            String accessToken = jwtTokenProvider.generateAccessToken(student.getId().toString());
            String refreshToken = jwtTokenProvider.generateRefreshToken(student.getId().toString());

            return OAuthLoginResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .user(mapToUserInfo(student))
                    .build();
        } catch (GeneralSecurityException | IOException e) {
            throw new AuthenticationExceptionHandler("구글 ID 토큰 검증 중 오류가 발생했어요.");
        }
    }

    @Transactional
    protected StudentEntity findOrCreateGoogleStudent(GoogleIdToken.Payload payload) {
        String oauthId = payload.getSubject();
        String email = payload.getEmail();
        String name = (String) payload.get("name");
        String studentId = email.substring(0, email.indexOf("@"));

        return studentRepository.findByOauthId(oauthId)
                .orElseGet(() -> studentRepository.save(StudentEntity.builder()
                        .oauthId(oauthId)
                        .oauthProvider(StudentEntity.Provider.google)
                        .studentId(studentId)
                        .stduentName(name) // 'stduentName'에서 'studentName'으로 수정
                        .build()));
    }

    public StudentEntity getAuthenticatedStudent() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationExceptionHandler("인증된 사용자가 아니예요.");
        }
        
        // UserDetails에서 학생 ID 추출 후 학생 조회
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String userId = userDetails.getUsername(); // JWT에서는 username에 userId를 저장했음
        
        return findById(Long.parseLong(userId));
    }

    public StudentEntity findById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("해당 학생을 찾을 수 없어요")
            );
    }

    public OAuthLoginResponse refreshToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new AuthenticationExceptionHandler("유효하지 않은 리프레시 토큰이에요.");
        }

        String userId = jwtTokenProvider.getUserIdFromToken(refreshToken);
        String newAccessToken = jwtTokenProvider.generateAccessToken(userId);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(userId);

        return OAuthLoginResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    private OAuthLoginResponse.UserInfo mapToUserInfo(StudentEntity student) {
        return OAuthLoginResponse.UserInfo.builder()
                .studentId(student.getStudentId())
                .studentName(student.getStduentName()) // 'getStduentName()'에서 'getStudentName()'으로 수정 // 'getStduentName()'에서 'getStudentName()'으로 수정
                .studentEmail(student.getStudentEmail())
                .build();
    }
}
