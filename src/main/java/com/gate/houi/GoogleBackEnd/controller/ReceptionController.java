package com.gate.houi.GoogleBackEnd.controller;

import com.gate.houi.GoogleBackEnd.apiPayload.ApiResponse;
import com.gate.houi.GoogleBackEnd.common.security.JwtTokenProvider;
import com.gate.houi.GoogleBackEnd.dto.req.ReceptionReqDto;
import com.gate.houi.GoogleBackEnd.dto.res.HistoryResDto;
import com.gate.houi.GoogleBackEnd.dto.res.MainResDto;
import com.gate.houi.GoogleBackEnd.service.ReceptionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reception")
@RequiredArgsConstructor
public class ReceptionController {

    private final ReceptionService receptionService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    @Operation(summary = "진료 접수", description = "증상내용 필요" )
    public ApiResponse<MainResDto.WaitInformation> register(@RequestBody ReceptionReqDto receptionReqDto) {
        Long userId = jwtTokenProvider.getUserIdFromToken();
        return ApiResponse.onSuccess(receptionService.RegisterReception(receptionReqDto, userId));
    }

    @GetMapping("/history")
    @Operation(summary = "진료 내역 조회", description = "증상내용 + 처방내용 + 접수일자 조회" )
    public ApiResponse<List<HistoryResDto>>  history() {
        Long userId = jwtTokenProvider.getUserIdFromToken();
        return ApiResponse.onSuccess(receptionService.getReceptionHistory(userId));
    }
}
