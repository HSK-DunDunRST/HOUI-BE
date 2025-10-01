package com.gate.houi.be.controller;

import com.gate.houi.be.apiPayload.ApiResponse;
import com.gate.houi.be.common.security.JwtTokenProvider;
import com.gate.houi.be.dto.req.ReceptionReqDto;
import com.gate.houi.be.dto.res.MainResDto;
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
    public ApiResponse<MainResDto.WaitInformation> register(@RequestBody ReceptionReqDto receptionReqDto) {
        Long userId = jwtTokenProvider.getUserIdFromToken();
        return ApiResponse.onSuccess(receptionService.RegisterReception(receptionReqDto, userId));
    }
}
