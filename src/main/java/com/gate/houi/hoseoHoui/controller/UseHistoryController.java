package com.gate.houi.hoseoHoui.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.gate.houi.hoseoHoui.domain.dtoSet.UseHistoryResponseDTO;
import com.gate.houi.hoseoHoui.service.UseHistoryService;

import lombok.RequiredArgsConstructor;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/history")
@RequiredArgsConstructor
public class UseHistoryController {

    @Autowired
    private final UseHistoryService useHistoryService;

    // 모든 이용내역 조회(관리자 페이지 전용)
    @GetMapping("/all")
    public ResponseEntity<List<UseHistoryResponseDTO>> getAllHistories() {
        return ResponseEntity.ok(useHistoryService.getHistoriesByStudentId(null)); // null은 모든 학생의 이용내역을 의미
    }

    // 각 학생의 이용내역 조회
    @GetMapping("/{studentId}")
    public ResponseEntity<List<UseHistoryResponseDTO>> getAllHistory(@PathVariable Long studentId, UseHistoryResponseDTO useHistoryResponseDTO) {
        return ResponseEntity.ok(useHistoryService.getHistoriesByStudentId(studentId));
    }
}
