package com.gate.houi.GoogleBackEnd.service;

import com.gate.houi.GoogleBackEnd.apiPayload.code.exception.BaseException;
import com.gate.houi.GoogleBackEnd.apiPayload.code.status.ErrorType;
import com.gate.houi.GoogleBackEnd.converter.NoticeConverter;
import com.gate.houi.GoogleBackEnd.dto.res.MainResDto;
import com.gate.houi.GoogleBackEnd.dto.res.NoticeResDto;
import com.gate.houi.GoogleBackEnd.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepository noticeRepository;

    @Override
    @Transactional(readOnly = true)
    public MainResDto.NoticeSummary getNoticeAtLast() {
        return noticeRepository.findFirstByOrderByCreatedAtDesc()
                .map(NoticeConverter::toLast)
                .orElseThrow(() -> new BaseException(ErrorType.NOT_FOUND_NOTICE_DATA));
    }

    @Override
    @Transactional(readOnly = true)
    public NoticeResDto getNoticeMore() {
       return noticeRepository.findFirstByOrderByCreatedAtDesc()
               .map(NoticeConverter::toMore)
               .orElseThrow(() -> new BaseException(ErrorType.NOT_FOUND_NOTICE_DATA));
    }

}
