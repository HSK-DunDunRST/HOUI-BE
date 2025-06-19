package com.gate.houi.hoseoHoui.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gate.houi.hoseoHoui.domain.dtoSet.UseHistoryResponseDTO;
import com.gate.houi.hoseoHoui.domain.entitySet.UseHistoryEntity;
import com.gate.houi.hoseoHoui.exception.DataNotFoundException;
import com.gate.houi.hoseoHoui.repository.UseHistoryRepository;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service  // 스프링 서비스 컴포넌트로 등록
@RequiredArgsConstructor
public class UseHistoryService {

    @Autowired
    private final UseHistoryRepository useHistoryRepository;

    // 모든 이용내역 조회 후 DTO 리스트로 반환
    @Transactional(readOnly = true)
    public List<UseHistoryResponseDTO> getAllHistories() {
        List<UseHistoryEntity> histories = useHistoryRepository.findAll();
        if (histories == null || histories.isEmpty()) {
            throw new DataNotFoundException("이용한 내역이 없어요."); // 예외 처리: 이용내역이 없을 경우
        }
        return histories.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // 특정 학생의 이용내역 조회 후 DTO 리스트로 반환
    @Transactional
    public List<UseHistoryResponseDTO> getHistoriesByStudentId(Long studentId) {
        List<UseHistoryEntity> histories = useHistoryRepository.findByStudentId(studentId);
        if (histories == null || histories.isEmpty()) {
            throw new DataNotFoundException("이용한 내역이 없어요."); // 예외 처리: 이용내역이 없을 경우
        }
        return histories.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // 이용내역 엔티티를 DTO로 변환하는 유틸리티 메서드
    private UseHistoryResponseDTO convertToDto(UseHistoryEntity useHistoryEntity) {
        return UseHistoryResponseDTO.builder()
                .id(useHistoryEntity.getId())
                .symptomsContent(useHistoryEntity.getSymptomsContent())
                .prescriptionContent(useHistoryEntity.getPrescriptionContent())
                .receptionCreatedAt(useHistoryEntity.getCreatedAt().toString())
            .build();
    }
}
