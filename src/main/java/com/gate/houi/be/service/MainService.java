package com.gate.houi.be.service;

import com.gate.houi.be.dto.res.MainResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MainService {

    private final NoticeService noticeService;
    private final ReceptionService receptionService;

    @Transactional(readOnly = true)
    public MainResDto getStatus(){
        MainResDto.NoticeSummary latestNotice = noticeService.getNoticeAtLast();
        MainResDto.WaitInformation waitCount = receptionService.waitCount();

        return MainResDto.builder()
                .noticeSummary(latestNotice)
                .waitInformation(waitCount)
                .build();
    }
}