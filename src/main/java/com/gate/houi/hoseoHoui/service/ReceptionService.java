package com.gate.houi.hoseoHoui.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gate.houi.hoseoHoui.domain.dtoSet.NoticeRequestDTO;
import com.gate.houi.hoseoHoui.domain.dtoSet.NoticeResponseDTO;
import com.gate.houi.hoseoHoui.domain.dtoSet.ReceptionRequestDTO;
import com.gate.houi.hoseoHoui.domain.dtoSet.ReceptionResponseDTO;
import com.gate.houi.hoseoHoui.domain.dtoSet.UseHistoryResponseDTO;
import com.gate.houi.hoseoHoui.domain.entitySet.NoticeEntity;
import com.gate.houi.hoseoHoui.domain.entitySet.ReceptionEntity;
import com.gate.houi.hoseoHoui.domain.entitySet.UseHistoryEntity;
import com.gate.houi.hoseoHoui.domain.enumSet.ReceptionType;
import com.gate.houi.hoseoHoui.exception.DataNotFoundException;
import com.gate.houi.hoseoHoui.exception.RequiredDataMissingException;
import com.gate.houi.hoseoHoui.repository.ReceptionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReceptionService {

    @Autowired
    public final ReceptionRepository receptionRepository;

    @Transactional(readOnly = true)
    // 모든 진료접수 조회 후 DTO 리스트로 반환
    // 테스트중 -> 관리자 페에지 전용
    public List<ReceptionResponseDTO> getAllReception() {
        return receptionRepository.findAll().stream()
                .map(this::convertToDto) // 엔티티를 DTO로 변환
                .collect(Collectors.toList());
    }

    @Transactional
    // 새로운 접수 객체 생성 후 DTO로 반환
    public ReceptionResponseDTO RegisterReception(ReceptionRequestDTO receptionRequestDTO) {
        // 필수 데이터(진료 증상)가 전달되지 않았으면 예외 발생
        if (receptionRequestDTO.getSymptomsContent().isEmpty()) {
            throw new RequiredDataMissingException();
        }
        
        // DTO로부터 NoticeEntity 생성
        ReceptionEntity receptionEntity = ReceptionEntity.builder()
                .symptomsContent(receptionRequestDTO.getSymptomsContent())
                .receptionStatus(ReceptionType.WAITING) // 기본 상태는 대기
                .build();
        
        // 엔티티 저장 후 결과 DTO로 변환하여 반환
        ReceptionEntity savedNotice = receptionRepository.save(receptionEntity);
        return convertToDto(savedNotice);
    }

    // 이용내역 엔티티를 DTO로 변환하는 유틸리티 메서드
    private ReceptionResponseDTO convertToDto(ReceptionEntity receptionEntity) {
        return ReceptionResponseDTO.builder()
                .id(receptionEntity.getId())
                .symptomsContent(receptionEntity.getSymptomsContent())
                .receptionStatus(receptionEntity.getReceptionStatus().toString())
                .receptionCreatedAt(receptionEntity.getCreatedAt().toString())
            .build();
    }
}
