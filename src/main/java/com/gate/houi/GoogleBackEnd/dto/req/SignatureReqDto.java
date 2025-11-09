package com.gate.houi.GoogleBackEnd.dto.req;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignatureReqDto {
    private MultipartFile file;   // 서명 파일 (하나만)
}
