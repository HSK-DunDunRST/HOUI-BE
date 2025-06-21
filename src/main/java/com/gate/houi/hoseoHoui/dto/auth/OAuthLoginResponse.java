package com.gate.houi.hoseoHoui.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OAuthLoginResponse {
    private String accessToken;
    private String refreshToken;
    private UserInfo user;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfo {
        private String studentId;    // 학번 필드 추가
        private String studentName;         // 이름 필드 추가
        private String studentEmail;        // 이메일 필드 추가
    }
}