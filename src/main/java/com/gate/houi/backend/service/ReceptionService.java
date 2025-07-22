package com.gate.houi.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gate.houi.backend.data.entityType.AccountEntity;
import com.gate.houi.backend.data.entityType.ReceptionEntity;
import com.gate.houi.backend.data.enumType.CampusType;
import com.gate.houi.backend.data.enumType.ReceptionType;
import com.gate.houi.backend.dto.reception.ReceptionHistoryResponseDTO;
import com.gate.houi.backend.dto.reception.ReceptionRegisterRequestDTO;
import com.gate.houi.backend.dto.reception.ReceptionRegisterResponseDTO;
import com.gate.houi.backend.repository.AccountRepository;
import com.gate.houi.backend.repository.ReceptionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReceptionService {

    @Autowired
    public final ReceptionRepository receptionRepository;
    private final AccountRepository studentRepository;

    @Transactional
    // 새로운 접수 객체 생성 후 DTO로 반환
    public ReceptionRegisterResponseDTO RegisterReception(ReceptionRegisterRequestDTO receptionRequestDTO, String oauthId) {
        // 필수 데이터(진료 증상)가 전달되지 않았으면 예외 발생
        if (receptionRequestDTO.getSymptomsContent().isEmpty()) {
            throw new RequiredDataMissingException();
        }
        
        // oauthId로 학생 정보 조회
        AccountEntity accountEntity = studentRepository.findByOauthId(oauthId)
                .orElseThrow(() -> new RuntimeException("학생 정보를 찾을 수 없습니다."));
        
        // DTO로부터 ReceptionEntity 생성
        ReceptionEntity receptionEntity = ReceptionEntity.builder()
                .symptomsContent(receptionRequestDTO.getSymptomsContent())
                .campusType(CampusType.ASAN) // 아산캠퍼스로 고정
                .receptionType(ReceptionType.WAITING) // 기본 상태는 대기
                .studentUuid(accountEntity.getAccountUuid()) // 학생 엔티티에서 UUID 추출
                .build();
        
        // 엔티티 저장 후 결과 DTO로 변환하여 반환
        receptionRepository.save(receptionEntity);
        return convertToDtoForRegistration(receptionEntity);
    }

    // 사용자의 모든 접수 내역 조회
    public List<ReceptionHistoryResponseDTO> getReceptionHistory(String oauthId) {
        // oauthId로 학생 정보 조회
        AccountEntity accountEntity = studentRepository.findByOauthId(oauthId)
                .orElseThrow(() -> new RuntimeException("학생 정보를 찾을 수 없습니다."));
        
        // 해당 학생의 모든 접수 내역 조회
        List<ReceptionEntity> receptions = receptionRepository.findByStudentUuidOrderByCreatedAtDesc(accountEntity.getAccountUuid());
        
        // DTO로 변환하여 반환
        return receptions.stream()
                .map(this::convertToDtoForHistory)
                .collect(Collectors.toList());
    }

    // 접수 내역 조회 시 사용하는 상세 DTO 변환 메서드
    private ReceptionHistoryResponseDTO convertToDtoForHistory(ReceptionEntity receptionEntity) {
        return ReceptionHistoryResponseDTO.builder()
            .id(receptionEntity.getId())
            .symptomsContent(receptionEntity.getSymptomsContent())
            .campusType(receptionEntity.getCampusType())
            .prescriptionContent(receptionEntity.getPrescriptionContent())
            .receptionType(receptionEntity.getReceptionType())
            .createdAt(receptionEntity.getCreatedAt())
            .updatedAt(receptionEntity.getUpdatedAt())
            .build();
    }

    // 접수 등록 시 사용하는 간단한 DTO 변환 메서드 (대기정보만 포함)
    private ReceptionRegisterResponseDTO convertToDtoForRegistration(ReceptionEntity receptionEntity) {
        // 대기 정보 계산
        Integer totalWaitingCount = null;
        // 대기 상태인 경우에만 대기 인원수 계산
        if (receptionEntity.getReceptionType() == ReceptionType.WAITING) {
            // 총 대기인원수 조회 (본인 제외하여 -1)
            long totalCount = receptionRepository.countByReceptionType(ReceptionType.WAITING);
            totalWaitingCount = (int) (totalCount - 1);
        }
        
        return ReceptionRegisterResponseDTO.builder()
            .id(receptionEntity.getId())
            .totalWaitingCount(totalWaitingCount)
            .build();
    }
}
