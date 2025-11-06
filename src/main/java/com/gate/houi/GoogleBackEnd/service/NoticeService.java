package com.gate.houi.GoogleBackEnd.service;

import com.gate.houi.GoogleBackEnd.dto.res.MainResDto;
import com.gate.houi.GoogleBackEnd.dto.res.NoticeResDto;

public interface NoticeService {
    MainResDto.NoticeSummary getNoticeAtLast();
    NoticeResDto getNoticeMore();
}
