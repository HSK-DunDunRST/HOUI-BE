package com.gate.houi.backend.controller;

import java.util.List;
import java.util.UUID;

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
import com.gate.houi.backend.dto.reception.ReceptionStatusDTO;
import com.gate.houi.backend.service.ReceptionService;
import com.gate.houi.backend.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/reception")
@RequiredArgsConstructor
public class ReceptionController {

    @Autowired
    private final ReceptionService receptionService;
    
    @Autowired
    private final UserService userService;

    // 새로운 진료접수 등록
    @PostMapping("/register")
    public ResponseEntity<ReceptionStatusDTO> addReception(@RequestBody ReceptionRegisterRequestDTO receptionRequestDTO,
                                                            @AuthenticationPrincipal UserDetails userDetails) {
        // OAuth ID로 UUID 조회 (보안상 분리)
        String oauthId = userDetails.getUsername();
        UUID studentUuid = userService.getStudentUuidByOauthId(oauthId);
        return ResponseEntity.ok(receptionService.RegisterReception(receptionRequestDTO, studentUuid));
    }

    // 사용자의 진료접수 내역 조회
    @GetMapping("/history")
    public ResponseEntity<List<UsageHistoryResponseDTO>> getReceptionHistory(@AuthenticationPrincipal UserDetails userDetails) {
        // OAuth ID로 UUID 조회 (보안상 분리)
        String oauthId = userDetails.getUsername();
        java.util.UUID studentUuid = userService.getStudentUuidByOauthId(oauthId);
        
        return ResponseEntity.ok(receptionService.getSuccessfulReceptions(studentUuid));
    }
}
