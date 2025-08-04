package com.gate.houi.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gate.houi.backend.data.enumType.ReceptionType;
import com.gate.houi.backend.dto.main.HomeResponseDTO;
import com.gate.houi.backend.dto.notice.NoticeResponseDTO;
import com.gate.houi.backend.dto.reception.ReceptionStatusDTO;
import com.gate.houi.backend.repository.ReceptionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MainService {
    
    private final NoticeService noticeService;
    private final ReceptionRepository receptionRepository;

    @Transactional(readOnly = true)
    public HomeResponseDTO getHomeData() {
        // 최신 공지사항 조회
        NoticeResponseDTO latestNotice = noticeService.getLatestNotice();
        
        // 현재 대기 인원 수 조회
        long waitingCount = receptionRepository.countByReceptionType(ReceptionType.WAITING);
        ReceptionStatusDTO receptionStatus = ReceptionStatusDTO.builder()
                .waitingPeople((int) waitingCount)
                .build();
        
        return HomeResponseDTO.builder()
                .notice(latestNotice)
                .reception(receptionStatus)
                .build();
    }
}
