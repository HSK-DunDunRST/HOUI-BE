package com.gate.houi.be.controller;

import com.gate.houi.be.apiPayload.ApiResponse;
import com.gate.houi.be.common.security.JwtTokenProvider;
import com.gate.houi.be.dto.req.ReceptionReqDto;
import com.gate.houi.be.service.ReceptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reception")
@RequiredArgsConstructor
public class ReceptionController {

    private final ReceptionService receptionService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public ApiResponse<String> register(@RequestBody ReceptionReqDto receptionReqDto) {
        Long userId = jwtTokenProvider.getUserIdFromToken();
        receptionService.RegisterReception(receptionReqDto, userId);
        return ApiResponse.onSuccess("성공적으로 접수가 되었어요.");
    }
}
