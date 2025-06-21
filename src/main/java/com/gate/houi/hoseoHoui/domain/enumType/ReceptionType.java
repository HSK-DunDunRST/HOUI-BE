package com.gate.houi.hoseoHoui.domain.enumType;

import lombok.Getter;

/**
 * 진료 접수 상태를 나타내는 열거형(Enum) 클래스
 * 
 * 환자의 진료 상태를 관리하기 위한 두 가지 상태를 정의:
 * - WAITING: 환자가 접수되어 진료를 기다리는 상태
 * - COMPLETED: 환자의 진료가 완료된 상태
 */
@Getter
public enum ReceptionType {
    /**
     * 환자가 진료를 대기 중인 상태
     */
    WAITING("대기중"),
    
    /**
     * 환자의 진료가 완료된 상태
     */
    COMPLETED("완료");

    /**
     * 접수 상태의 한글 표현
     */
    private final String status;

    /**
     * ReceptionType 생성자
     *
     * @param receptionStatus 접수 상태의 한글 표현 문자열
     */
    ReceptionType(String receptionStatus) {
        this.status = receptionStatus;
    }
}
