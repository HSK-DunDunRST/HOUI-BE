package com.gate.houi.be.controller;

import com.gate.houi.be.apiPayload.ApiResponse;
import com.gate.houi.be.common.security.JwtTokenProvider;
import com.gate.houi.be.dto.res.SignatureResDto;
import com.gate.houi.be.service.SignatureServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/signature")
@RequiredArgsConstructor
public class SignatureController {

    private final SignatureServiceImpl signatureService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/upload")
    public ApiResponse<SignatureResDto> uploadSignature(@RequestParam("signature") MultipartFile file) throws IOException {
        Long userId = jwtTokenProvider.getUserIdFromToken();
        return ApiResponse.onSuccess(signatureService.uploadSignature(userId, file));
    }
}