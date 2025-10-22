package com.gate.houi.be.controller;

import com.gate.houi.be.apiPayload.ApiResponse;
import com.gate.houi.be.common.security.JwtTokenProvider;
import com.gate.houi.be.dto.res.MainResDto;
import com.gate.houi.be.service.MainService;
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
    public ApiResponse<MainResDto> getStatus() {
        return ApiResponse.onSuccess(mainService.getStatus());
    }
}
