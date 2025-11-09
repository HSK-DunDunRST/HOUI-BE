package com.gate.houi.GoogleBackEnd.converter;

import com.gate.houi.GoogleBackEnd.dto.res.SignatureResDto;
import com.gate.houi.GoogleBackEnd.entity.SignatureEntity;

public class SignatureConverter {
    public static SignatureResDto toDto(SignatureEntity signatureEntity) {
        return SignatureResDto.builder()
                .id(signatureEntity.getId())
                .studentId(signatureEntity.getStudentId())
                .signatureUrl(signatureEntity.getSignatureUrl())
                .build();
    }
}
