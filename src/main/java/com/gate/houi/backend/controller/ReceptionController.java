package com.gate.houi.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gate.houi.backend.dto.history.UsageHistoryResponseDTO;
import com.gate.houi.backend.dto.reception.ReceptionRegisterRequestDTO;
import com.gate.houi.backend.dto.reception.ReceptionRegisterResponseDTO;
import com.gate.houi.backend.service.ReceptionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/reception")
@RequiredArgsConstructor
public class ReceptionController {

    @Autowired
    private final ReceptionService receptionService;

    // 새로운 진료접수 등록
    @PostMapping("/register")
    public ResponseEntity<ReceptionRegisterResponseDTO> addReception(@RequestBody ReceptionRegisterRequestDTO receptionRequestDTO,
                                                            @AuthenticationPrincipal UserDetails userDetails) {
        // UserDetails에서 OAuth ID를 추출 (JWT 토큰에서 추출한 사용자 식별자)
        String oauthId = userDetails.getUsername();
        
        return ResponseEntity.ok(receptionService.RegisterReception(receptionRequestDTO, oauthId));
    }

    // 사용자의 진료접수 내역 조회
    @GetMapping("/history")
    public ResponseEntity<List<UsageHistoryResponseDTO>> getReceptionHistory(@AuthenticationPrincipal UserDetails userDetails) {
        // UserDetails에서 OAuth ID를 추출 (JWT 토큰에서 추출한 사용자 식별자)
        String oauthId = userDetails.getUsername();
        
        return ResponseEntity.ok(receptionService.getSuccessfulReceptions(oauthId));
    }
}
