package com.gate.houi.be.service;

import com.gate.houi.be.dto.res.MainResDto;

public interface NoticeService {
    MainResDto.NoticeSummary getNoticeAtLast();
}
