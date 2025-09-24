package com.gate.houi.be.dto.res;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoogleUserInfoResDto {
    private UUID studentUuid;
    private String studentId;
    private String studentName;
    private String studentEmail;
    private String oauthProvider;
    private LocalDateTime createdAt;
}
