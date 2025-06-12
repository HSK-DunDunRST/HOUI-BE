package com.gate.houi.hoseoHoui.domain.enumSet;

import org.springframework.http.HttpStatus;
import lombok.Getter;

@Getter
public enum ErrorType {
    
    // Error Code - 400
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청이에요."),
    MISSING_REQUIRED_FIELDS(HttpStatus.BAD_REQUEST, "입력 항목을 다시 확인해주세요."),
    // Error code - 401
    NOT_VALID_TOKEN(HttpStatus.FORBIDDEN, "토큰이 유효하지 않아요."),
    // Error code - 404
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "학생정보를 찾을 수 없어요."),
    NOT_FOUND_DATA(HttpStatus.NOT_FOUND, "등록된 데이터를 찾을 수 없어요."),
    NOT_FOUND_SIGNTURE(HttpStatus.NOT_FOUND, "서명을 불러올 수 없어요."),
    // Error code - 500
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "요청을 처리하는 도중에 문제가 발생했어요!"),
    DATA_LOADING_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "데이터를 불러오는데 실패했어요.");

    private HttpStatus errorCode;
    private String errorMessage;

    ErrorType(HttpStatus errorCode, String errorMessage){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}