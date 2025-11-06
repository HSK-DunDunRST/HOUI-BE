package com.gate.houi.GoogleBackEnd.service;

import com.gate.houi.GoogleBackEnd.dto.res.MainResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MainService {

    private final NoticeService noticeService;

    @Transactional(readOnly = true)
    public MainResDto getStatus(){

        MainResDto.NoticeSummary latestNotice = noticeService.getNoticeAtLast();

        return MainResDto.builder()
                .noticeSummary(latestNotice)
                .waitInformation(null)
                .build();
    }
}