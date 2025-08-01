package com.gate.houi.backend.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gate.houi.backend.data.entityType.StudentEntity;
import com.gate.houi.backend.data.entityType.ReceptionEntity;
import com.gate.houi.backend.data.enumType.CampusType;
import com.gate.houi.backend.data.enumType.ReceptionType;
import com.gate.houi.backend.dto.history.AllUsageHistoryResponseDTO;
import com.gate.houi.backend.dto.history.UsageHistoryResponseDTO;
import com.gate.houi.backend.dto.reception.ReceptionRegisterRequestDTO;
import com.gate.houi.backend.dto.reception.ReceptionRegisterResponseDTO;
import com.gate.houi.backend.exception.NoticeDataNotFoundException;
import com.gate.houi.backend.exception.RequiredDataMissingException;
import com.gate.houi.backend.repository.StudentRepository;
import com.gate.houi.backend.repository.ReceptionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReceptionService {

    @Autowired
    public final ReceptionRepository receptionRepository;
    private final StudentRepository studentRepository;

    @Transactional
    // 새로운 접수 객체 생성 후 DTO로 반환
    public ReceptionRegisterResponseDTO RegisterReception(ReceptionRegisterRequestDTO receptionRequestDTO, UUID studentUuid) {
        // 필수 데이터(진료 증상)가 전달되지 않았으면 예외 발생
        if (receptionRequestDTO.getSymptomsContent().isEmpty()) {
            throw new RequiredDataMissingException();
        }

        // studentUuid로 학생 정보 조회 (존재 여부 확인용)
        StudentEntity accountEntity = studentRepository.findByStudentUuid(studentUuid)
                .orElseThrow(() -> new RuntimeException("학생 정보를 찾을 수 없습니다."));
        
        // DTO로부터 ReceptionEntity 생성
        ReceptionEntity receptionEntity = ReceptionEntity.builder()
                .symptomsContent(receptionRequestDTO.getSymptomsContent())
                .campusType(CampusType.ASAN) // 아산캠퍼스로 고정
                .receptionType(ReceptionType.WAITING) // 기본 상태는 대기
                .studentUuid(accountEntity.getStudentUuid()) // studentUuid 사용
                .build();
        
        // 엔티티 저장 후 결과 DTO로 변환하여 반환
        receptionRepository.save(receptionEntity);
        return convertToDtoForRegistration(receptionEntity);
    }

    // SUCCESSFUL 상태의 접수만 조회 후 DTO 리스트로 반환 (처방완료 이용내역만 조회)
    @Transactional(readOnly = true)
    public List<UsageHistoryResponseDTO> getSuccessfulReceptions(UUID studentUuid) {
        // studentUuid로 학생 정보 조회 (존재 여부 확인용)
        StudentEntity accountEntity = studentRepository.findByStudentUuid(studentUuid)
                .orElseThrow(() -> new RuntimeException("학생 정보를 찾을 수 없습니다."));
        
        // 해당 사용자의 처방 완료된 접수 내역 조회 (studentUuid 사용)
        List<ReceptionEntity> receptions = receptionRepository.findByStudentUuidAndReceptionTypeOrderByCreatedAt(accountEntity.getStudentUuid(), ReceptionType.SUCCESSFUL);

        return receptions.stream()
                .map(this::convertToDtoForHistory)
                .collect(Collectors.toList());
    }


    // 모든 접수 내역 조회 시 사용하는 상세 DTO 변환 메서드
    private AllUsageHistoryResponseDTO convertToDto(ReceptionEntity receptionEntity) {
        // studentUuid로 학생 정보 조회
        StudentEntity accountEntity = studentRepository.findByStudentUuid(receptionEntity.getStudentUuid())
                .orElseThrow(() -> new NoticeDataNotFoundException());
        
        return AllUsageHistoryResponseDTO.builder()
            .id(receptionEntity.getId())
            .studentId(accountEntity.getStudentId())
            .studentName(accountEntity.getStudentName())
            .symptomsContent(receptionEntity.getSymptomsContent())
            .prescriptionContent(receptionEntity.getPrescriptionContent())
            .campusType(receptionEntity.getCampusType())
            .receptionType(receptionEntity.getReceptionType())
            .createdAt(receptionEntity.getCreatedAt())
            .updatedAt(receptionEntity.getUpdatedAt())
            .build();
    }

    // 접수 내역 조회 시 사용하는 상세 DTO 변환 메서드
    private UsageHistoryResponseDTO convertToDtoForHistory(ReceptionEntity receptionEntity) {
        return UsageHistoryResponseDTO.builder()
            .id(receptionEntity.getId())
            .symptomsContent(receptionEntity.getSymptomsContent())
            .campusType(receptionEntity.getCampusType())
            .prescriptionContent(receptionEntity.getPrescriptionContent())
            .receptionType(receptionEntity.getReceptionType())
            .createdAt(receptionEntity.getCreatedAt())
            .updatedAt(receptionEntity.getUpdatedAt())
            .build();
    }

    // 접수 등록 시 사용하는 간단한 DTO 변환 메서드 (대기인원수만 포함)
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
            .waitingPeople(totalWaitingCount)
            .build();
    }
}
