package com.gate.houi.backend.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gate.houi.backend.data.entityType.StudentEntity;
import com.gate.houi.backend.dto.student.StudentProfileResponseDTO;
import com.gate.houi.backend.exception.UserNotFoundException;
import com.gate.houi.backend.repository.StudentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final StudentRepository accountRepository;

    @Transactional(readOnly = true)
    public StudentProfileResponseDTO getAccountProfile(String oauthId) {
        StudentEntity userEntity = accountRepository.findByOauthId(oauthId)
                .orElseThrow(() -> new UserNotFoundException());

        return convertToDto(userEntity);
    }

    @Transactional(readOnly = true)
    public StudentProfileResponseDTO getDetailedAccountProfile(String oauthId) {
        StudentEntity userEntity = accountRepository.findByOauthId(oauthId)
                .orElseThrow(() -> new UserNotFoundException());

        return StudentProfileResponseDTO.builder()
                .studentUuid(userEntity.getStudentUuid())
                .studentId(userEntity.getStudentId())
                .studentName(userEntity.getStudentName())
                .oauthProvider(userEntity.getOauthProvider())
                .createdAt(userEntity.getCreatedAt())
                .build();
    }

    /**
     * OAuth ID로 학생의 UUID를 조회하는 유틸리티 메서드
     * 보안상 OAuth ID는 JWT 토큰용, UUID는 데이터베이스 처리용으로 분리
     */
    @Transactional(readOnly = true)
    public UUID getStudentUuidByOauthId(String oauthId) {
        StudentEntity userEntity = accountRepository.findByOauthId(oauthId)
                .orElseThrow(() -> new UserNotFoundException());
        
        return userEntity.getStudentUuid();
    }

    private StudentProfileResponseDTO convertToDto(StudentEntity accountEntity) {
        return StudentProfileResponseDTO.builder()
                .studentUuid(accountEntity.getStudentUuid())
                .studentId(accountEntity.getStudentId())
                .studentName(accountEntity.getStudentName())
                .oauthProvider(accountEntity.getOauthProvider())
                .createdAt(accountEntity.getCreatedAt())
                .build();
    }
}
