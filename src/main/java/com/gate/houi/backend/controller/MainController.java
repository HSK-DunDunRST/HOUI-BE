package com.gate.houi.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gate.houi.backend.dto.main.HomeResponseDTO;
import com.gate.houi.backend.service.MainService;

import lombok.RequiredArgsConstructor;

@RestController

@RequiredArgsConstructor
// @PreAuthorize("isAuthenticated()") // 모든 엔드포인트는 인증 필요
public class MainController {
    
    private final MainService mainService;

    @GetMapping
    public ResponseEntity<HomeResponseDTO> getMainInfo() {
        return ResponseEntity.ok(mainService.getHomeData());
    }
}
