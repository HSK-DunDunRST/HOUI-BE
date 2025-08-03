package com.gate.houi.backend.controller;

import java.io.IOException;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.gate.houi.backend.service.SignatureService;
import com.gate.houi.backend.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/signature")
@RequiredArgsConstructor
public class SignatureController {

    private final SignatureService signatureService;
    private final UserService userService;

    /**
     * 학생의 서명 이미지 업로드
     */
    @PostMapping("/upload")
    public ResponseEntity<String> uploadSignature(
            @RequestParam("signature") MultipartFile file,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            // OAuth ID로 UUID 조회
            String oauthId = userDetails.getUsername();
            UUID studentUuid = userService.getStudentUuidByOauthId(oauthId);
            
            String fileUrl = signatureService.uploadSignature(studentUuid, file);
            return ResponseEntity.ok(fileUrl);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("서명 이미지 업로드에 실패했습니다: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("오류가 발생했습니다: " + e.getMessage());
        }
    }

    /**
     * 학생의 서명 이미지 조회
     */
    @GetMapping
    public ResponseEntity<String> getSignature(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            // OAuth ID로 UUID 조회
            String oauthId = userDetails.getUsername();
            UUID studentUuid = userService.getStudentUuidByOauthId(oauthId);
            
            String signatureUrl = signatureService.getSignatureByStudentUuid(studentUuid);
            if (signatureUrl != null) {
                return ResponseEntity.ok(signatureUrl);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("오류가 발생했습니다: " + e.getMessage());
        }
    }

    /**
     * 학생의 서명 이미지 삭제
     */
    @DeleteMapping
    public ResponseEntity<String> deleteSignature(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            // OAuth ID로 UUID 조회
            String oauthId = userDetails.getUsername();
            UUID studentUuid = userService.getStudentUuidByOauthId(oauthId);
            
            signatureService.deleteSignature(studentUuid);
            return ResponseEntity.ok("서명 이미지가 삭제되었습니다.");
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("서명 이미지 삭제에 실패했습니다: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("오류가 발생했습니다: " + e.getMessage());
        }
    }
}
