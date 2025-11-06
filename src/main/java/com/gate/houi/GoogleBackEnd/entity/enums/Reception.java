package com.gate.houi.GoogleBackEnd.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum Reception {

    WAITING("대기"),
    SUCCESSFUL("완료");

    private final String status;

}
