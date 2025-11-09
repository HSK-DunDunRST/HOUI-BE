package com.gate.houi.GoogleBackEnd.converter;

import com.gate.houi.GoogleBackEnd.dto.res.MainResDto;

public class ReceptionConverter {
    public static MainResDto.WaitInformation toWait(int waitCount) {
        return MainResDto.WaitInformation.builder()
                .waitCount(waitCount)
                .build();
    }
}
