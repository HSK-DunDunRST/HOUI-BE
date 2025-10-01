package com.gate.houi.be.converter;

import com.gate.houi.be.dto.req.SignatureReqDto;
import com.gate.houi.be.dto.res.SignatureResDto;
import com.gate.houi.be.entity.SignatureEntity;

public class SignatureConverter {
    public static SignatureResDto toDto(SignatureEntity signatureEntity) {
        return SignatureResDto.builder()
                .id(signatureEntity.getId())
                .studentId(signatureEntity.getStudentId())
                .signatureUrl(signatureEntity.getSignatureUrl())
                .build();
    }
}
