package com.gate.houi.hoseoHoui.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gate.houi.hoseoHoui.dto.notice.NoticeRequestDTO;
import com.gate.houi.hoseoHoui.dto.notice.NoticeResponseDTO;
import com.gate.houi.hoseoHoui.service.NoticeService;

import lombok.RequiredArgsConstructor;



@RestController
@RequestMapping("/notice")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()") // 기본적으로 모든 엔드포인트는 인증 필요
public class NoticeController {

    @Autowired
    private final NoticeService noticeService;

    // 최신 공지사항 조회
    @GetMapping
    public ResponseEntity<NoticeResponseDTO> getNotice(){
        return ResponseEntity.ok(noticeService.getLatestNotice());
    }
    // 모든 공지사항 조회
    @GetMapping("/all")
    public ResponseEntity<List<NoticeResponseDTO>> getAllNotice(){
        return ResponseEntity.ok(noticeService.getAllNotices());
    }
    // 공지사항 등록
    @PostMapping("/add")
    public ResponseEntity<NoticeResponseDTO> addNotice(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestBody NoticeRequestDTO noticeRequestDTO) {
        
        System.out.println("현재 인증된 사용자: " + userDetails.getUsername());
        return ResponseEntity.ok(noticeService.createNotice(noticeRequestDTO));
    }
    // 공지사항 수정
    @PatchMapping("/edit/{id}")
    public ResponseEntity<NoticeResponseDTO> updateNotice(@PathVariable Long id, @RequestBody NoticeRequestDTO noticeRequestDTO) {
        return ResponseEntity.ok(noticeService.updateNotice(id, noticeRequestDTO));
    }

    // 공지사항 삭제
    @DeleteMapping("/del/{id}")
    public ResponseEntity<Void> deleteNotice(@PathVariable Long id) {
        noticeService.deleteNotice(id);
        return ResponseEntity.noContent().build();
    }
    
}
