package com.gate.houi.be.converter;

import com.gate.houi.be.dto.res.MainResDto;

public class ReceptionConverter {
    public static MainResDto.WaitInformation toWait(int waitCount) {
        return MainResDto.WaitInformation.builder()
                .waitCount(waitCount)
                .build();
    }
}
