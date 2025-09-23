package com.gate.houi.be.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum Reception {

    WAITING("대기"),
    SUCCESSFUL("완료");

    private final String status;

}
