package com.gate.houi.hoseoHoui.dto.history;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponseDTO {
    private boolean authenticated;
    private String studentId;
    private String name;
    private String email;
    private List<String> authorities;
    private String token; // JWT 토큰 필드 추가
}