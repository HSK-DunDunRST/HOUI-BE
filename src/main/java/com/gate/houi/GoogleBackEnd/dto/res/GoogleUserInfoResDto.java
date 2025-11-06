package com.gate.houi.GoogleBackEnd.dto.res;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoogleUserInfoResDto {
    private String id;
    private String name;
    private String email;
    private Boolean verified_email;
}
