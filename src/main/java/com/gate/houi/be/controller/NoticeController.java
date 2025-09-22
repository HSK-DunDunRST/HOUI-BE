package com.gate.houi.be.controller;

import com.gate.houi.be.apiPayload.ApiResponse;
import com.gate.houi.be.dto.res.NoticeResDto;
import com.gate.houi.be.service.NoticeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notice")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeServiceImpl noticeServiceImpl;

    @GetMapping
    public ApiResponse<NoticeResDto> getNotice() {
        return ApiResponse.onSuccess(noticeServiceImpl.getNoticeAtLast());
    }
}
