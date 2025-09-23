package com.gate.houi.be.service;

import com.gate.houi.be.apiPayload.code.exception.BaseException;
import com.gate.houi.be.apiPayload.code.status.ErrorType;
import com.gate.houi.be.converter.NoticeConverter;
import com.gate.houi.be.dto.res.NoticeResDto;
import com.gate.houi.be.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepository noticeRepository;

    @Override
    public NoticeResDto getNoticeAtLast() {
        return noticeRepository.findFirstByOrderByCreatedAtDesc()
                .map(NoticeConverter::toLast)
                .orElseThrow(() -> new BaseException(ErrorType.NOT_FOUND_NOTICE_DATA));
    }

}
