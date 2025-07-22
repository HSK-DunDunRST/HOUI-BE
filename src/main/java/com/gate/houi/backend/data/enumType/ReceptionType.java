package com.gate.houi.backend.data.enumType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReceptionType {
    
    WAITING("대기"),
    SUCCESSFUL("완료");
    
    private final String status;

}
