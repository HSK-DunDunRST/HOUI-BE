package com.gate.houi.hoseoHoui.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gate.houi.hoseoHoui.domain.dtoSet.NoticeRequestDTO;
import com.gate.houi.hoseoHoui.domain.dtoSet.NoticeResponseDTO;
import com.gate.houi.hoseoHoui.domain.entitySet.NoticeEntity;
import com.gate.houi.hoseoHoui.exception.DataNotFoundException;
import com.gate.houi.hoseoHoui.exception.RequiredDataMissingException;
import com.gate.houi.hoseoHoui.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;

@Service  // 스프링 서비스 컴포넌트로 등록
@RequiredArgsConstructor  // final 필드에 대해 생성자 자동 주입
public class NoticeService {
    
    private final NoticeRepository noticeRepository; // NoticeRepository 주입

    @Transactional(readOnly = true)
    // 모든 공지사항 조회 후 DTO 리스트로 반환
    public List<NoticeResponseDTO> getAllNotices() {
        return noticeRepository.findAll().stream()
                .map(this::convertToDto) // 엔티티를 DTO로 변환
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    // 가장 최신의 공지사항 조회 후 DTO로 반환
    public NoticeResponseDTO getLatestNotice() {
        // 최신 공지사항을 검색하고 없으면 예외 발생
        NoticeEntity notice = noticeRepository.findFirstByOrderByCreatedAtDesc()
                .orElseThrow(() -> new DataNotFoundException());
        return convertToDto(notice);
    }

    @Transactional
    // 새로운 공지사항 생성 후 DTO로 반환
    public NoticeResponseDTO createNotice(NoticeRequestDTO noticeRequestDTO) {
        // 필수 데이터(공지 제목)가 전달되지 않았으면 예외 발생
        if (noticeRequestDTO.getNoticeTitle() == null || noticeRequestDTO.getNoticeTitle().isEmpty()) {
            throw new RequiredDataMissingException();
        }
        
        // DTO로부터 NoticeEntity 생성
        NoticeEntity notice = NoticeEntity.builder()
                .noticeTitle(noticeRequestDTO.getNoticeTitle())
                .noticeContent(noticeRequestDTO.getNoticeContent())
                .build();
        
        // 엔티티 저장 후 결과 DTO로 변환하여 반환
        NoticeEntity savedNotice = noticeRepository.save(notice);
        return convertToDto(savedNotice);
    }

    @Transactional
    // 기존 공지사항 업데이트 후 DTO로 반환
    public NoticeResponseDTO updateNotice(Long id, NoticeRequestDTO noticeRequestDTO) {
        // 해당 id의 공지사항을 조회하고 없으면 예외 발생
        NoticeEntity notice = noticeRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException());
        // 전달 받은 데이터로 제목과 내용을 업데이트
        notice.setNoticeTitle(noticeRequestDTO.getNoticeTitle());
        notice.setNoticeContent(noticeRequestDTO.getNoticeContent());
        
        // 엔티티 저장 후 결과 DTO로 변환하여 반환
        NoticeEntity updatedNotice = noticeRepository.save(notice);
        return convertToDto(updatedNotice);
    }

    @Transactional
    // 공지사항 삭제 후 삭제된 엔티티 정보를 DTO로 반환
    public NoticeResponseDTO deleteNotice(Long id) {
        // 해당 id의 공지사항을 조회하고 없으면 예외 발생
        NoticeEntity notice = noticeRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException());
        
        // 공지사항 삭제
        noticeRepository.delete(notice);
        return convertToDto(notice);
    }

    // 공지사항 엔티티를 DTO로 변환하는 유틸리티 메서드
    private NoticeResponseDTO convertToDto(NoticeEntity notice) {
        return NoticeResponseDTO.builder()
                .id(notice.getId())
                .noticeTitle(notice.getNoticeTitle())
                .noticeContent(notice.getNoticeContent())
                // 생성일자를 문자열로 변환하여 저장
                .noticeCreatedAt(notice.getCreatedAt().toString())
                .build();
    }
}