package com.gate.houi.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gate.houi.backend.data.enumType.ErrorType;
import com.gate.houi.backend.dto.notice.NoticeRequestDTO;
import com.gate.houi.backend.dto.notice.NoticeResponseDTO;
import com.gate.houi.backend.exception.ErrorResponse;
import com.gate.houi.backend.service.NoticeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/notice")
@RequiredArgsConstructor
// @PreAuthorize("isAuthenticated()") // 기본적으로 모든 엔드포인트는 인증 필요
public class NoticeController {

    @Autowired
    private final NoticeService noticeService;

    // 최신 공지사항 조회
    @GetMapping
    public ResponseEntity<?> getNotice(@RequestHeader("Authorization") String accessToken) {
        // 인증은 JwtAuthenticationFilter에서 처리되므로 별도로 헤더를 받을 필요 없음
        NoticeResponseDTO notice = noticeService.getLatestNotice();
        if (notice == null) {
            // 공지사항이 없을 때 200 상태로 에러 메시지 반환
            return ResponseEntity.ok(ErrorResponse.of(ErrorType.NO_NOTICE_AVAILABLE));
        }
        return ResponseEntity.ok(notice);
    }
    // 모든 공지사항 조회
    @GetMapping("/all")
    public ResponseEntity<?> getAllNotice(@RequestHeader("Authorization") String accessToken){
        List<NoticeResponseDTO> notices = noticeService.getAllNotices();
        if (notices.isEmpty()) {
            // 공지사항이 없을 때 200 상태로 에러 메시지 반환
            return ResponseEntity.ok(ErrorResponse.of(ErrorType.NO_NOTICE_AVAILABLE));
        }
        return ResponseEntity.ok(notices);
    }
    //! 여기 밑은 관리자 전용 컨트롤러로 분리 예정
    // 공지사항 등록
    @PostMapping("/add")
    public ResponseEntity<NoticeResponseDTO> addNotice(
        @RequestHeader("Authorization") String accessToken,
        // @AuthenticationPrincipal UserDetails userDetails,
        @RequestBody NoticeRequestDTO noticeRequestDTO) {
        
        // System.out.println("현재 인증된 사용자: " + userDetails.getUsername());
        return ResponseEntity.ok(noticeService.createNotice(noticeRequestDTO));
    }
    // 공지사항 수정
    @PatchMapping("/edit/{id}")
    public ResponseEntity<NoticeResponseDTO> updateNotice(
        @RequestHeader("Authorization") String accessToken,
        @PathVariable Long id, 
        @RequestBody NoticeRequestDTO noticeRequestDTO) {
        return ResponseEntity.ok(noticeService.updateNotice(id, noticeRequestDTO));
    }
    // 공지사항 삭제
    @DeleteMapping("/del/{id}")
    public ResponseEntity<Void> deleteNotice(
        @RequestHeader("Authorization") String accessToken,
        @PathVariable Long id) {
        noticeService.deleteNotice(id);
        return ResponseEntity.noContent().build();
    }
    
}
