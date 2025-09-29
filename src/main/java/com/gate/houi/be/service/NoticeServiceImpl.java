package com.gate.houi.be.service;

import com.gate.houi.be.apiPayload.code.exception.BaseException;
import com.gate.houi.be.apiPayload.code.status.ErrorType;
import com.gate.houi.be.converter.NoticeConverter;
import com.gate.houi.be.dto.res.MainResDto;
import com.gate.houi.be.dto.res.NoticeResDto;
import com.gate.houi.be.repository.NoticeRepository;
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

}
