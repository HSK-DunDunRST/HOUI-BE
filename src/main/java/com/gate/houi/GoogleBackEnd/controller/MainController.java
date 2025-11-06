package com.gate.houi.GoogleBackEnd.controller;

import com.gate.houi.GoogleBackEnd.apiPayload.ApiResponse;
import com.gate.houi.GoogleBackEnd.dto.res.MainResDto;
import com.gate.houi.GoogleBackEnd.service.MainService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/status")
@RequiredArgsConstructor
public class MainController {
    private final MainService mainService;

    @GetMapping
    @Operation(summary = "메인화면 기본정보 조회", description = "공지사항 제목 + 대기인원 데이터 조회" )
    public ApiResponse<MainResDto> getStatus() {
        return ApiResponse.onSuccess(mainService.getStatus());
    }
}
