package com.gate.houi.GoogleBackEnd.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TokenReqDto {

    @Getter
    @NoArgsConstructor
    public static class AccessTokenReq {
        @NotBlank(message = "엑세스 토큰은 필수입니다.")
        private String idToken;
    }
}