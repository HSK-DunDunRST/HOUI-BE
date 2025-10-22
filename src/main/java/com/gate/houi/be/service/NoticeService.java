package com.gate.houi.be.service;

import com.gate.houi.be.dto.res.MainResDto;
import com.gate.houi.be.dto.res.NoticeResDto;

public interface NoticeService {
    MainResDto.NoticeSummary getNoticeAtLast();
    NoticeResDto getNoticeMore();
}
