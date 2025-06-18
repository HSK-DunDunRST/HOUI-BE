package com.gate.houi.hoseoHoui.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gate.houi.hoseoHoui.domain.dtoSet.ReceptionRequestDTO;
import com.gate.houi.hoseoHoui.domain.dtoSet.ReceptionResponseDTO;
import com.gate.houi.hoseoHoui.service.ReceptionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/reception")
@RequiredArgsConstructor
public class ReceptionController {

    @Autowired
    private final ReceptionService receptionService;

    // 모든 진료접수 조회(관리자 페이지 전용)
    @GetMapping
    public ResponseEntity<List<ReceptionResponseDTO>> getReceptions(){
        return ResponseEntity.ok(receptionService.getAllReception());
    }

    // 새로운 진료접수 등록
    @PostMapping("/register")
    public ResponseEntity<ReceptionResponseDTO> addReception(@RequestBody ReceptionRequestDTO receptionRequestDTO) {
        return ResponseEntity.ok(receptionService.RegisterReception(receptionRequestDTO));
    }
}
