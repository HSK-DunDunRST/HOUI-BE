package com.gate.houi.GoogleBackEnd.dto.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

public class UserDtoRes {

    @Data
    @AllArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL) // null 필드는 응답에서 제외
    public static class UserLoginRes {
        private String name;
        private String accessToken;
        private String refreshToken; // 웹에선 null로 세팅해서 숨김
    }

    @Data
    @AllArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL) // null 필드는 응답에서 제외
    public static class UserInfoRes {
        private String studentId;
        private String userName;
        private String campusType;
    }
}
