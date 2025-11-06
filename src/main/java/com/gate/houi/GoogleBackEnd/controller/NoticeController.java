package com.gate.houi.GoogleBackEnd.controller;

import com.gate.houi.GoogleBackEnd.apiPayload.ApiResponse;
import com.gate.houi.GoogleBackEnd.common.security.JwtTokenProvider;
import com.gate.houi.GoogleBackEnd.dto.res.NoticeResDto;
import com.gate.houi.GoogleBackEnd.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notice")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/detail")
    @Operation(summary = "공지시항 상세 조회", description = "제목 + 내용 + 등록일자" )
    public ApiResponse<NoticeResDto> getNoticeDetail(){
        Long userId = jwtTokenProvider.getUserIdFromToken();
        return ApiResponse.onSuccess(noticeService.getNoticeMore());
    }
}

