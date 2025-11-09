package com.gate.houi.GoogleBackEnd.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignatureResDto {
    private Long id;          // PK
    private String studentId;      // 누구의 서명인지
    private String userCampus;
    private String signatureUrl;   // 접근 가능한 URL
}