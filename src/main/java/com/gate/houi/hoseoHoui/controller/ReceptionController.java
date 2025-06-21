package com.gate.houi.hoseoHoui.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gate.houi.hoseoHoui.dto.reception.ReceptionRequestDTO;
import com.gate.houi.hoseoHoui.dto.reception.ReceptionResponseDTO;
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
    public ResponseEntity<ReceptionResponseDTO> addReception(@RequestBody ReceptionRequestDTO receptionRequestDTO,
                                                            @AuthenticationPrincipal OAuth2User oauth2User) {
                                                                 // OAuth2User에서 이메일과 이름 정보 추출
        String studentId = (String) oauth2User.getAttribute("studentId");
        String studentName = (String) oauth2User.getAttribute("name");
        
        return ResponseEntity.ok(receptionService.RegisterReception(receptionRequestDTO, studentId, studentName));
    }
}
