package com.gate.houi.hoseoHoui.domain.enumType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorType {
    
    // Error Code - 400
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청이에요."),
    MISSING_REQUIRED_FIELDS(HttpStatus.BAD_REQUEST, "입력 항목을 다시 확인해주세요."),
    // Error code - 401
    NOT_VALID_TOKEN(HttpStatus.UNAUTHORIZED, "인증이 필요합니다. 로그인 후 이용해주세요."),
    // 인증 관련 에러
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "인증되지 않은 접근입니다. 구글 로그인이 필요합니다."),
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "인증 처리 중 오류가 발생했습니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "인증 토큰이 만료되었습니다. 다시 로그인해주세요."),
    // Error code - 404
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "학생정보를 찾을 수 없어요."),
    NOT_FOUND_DATA(HttpStatus.NOT_FOUND, "등록된 데이터를 찾을 수 없어요."),
    NOT_FOUND_SIGNTURE(HttpStatus.NOT_FOUND, "서명을 불러올 수 없어요."),
    // Error code - 500
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "요청을 처리하는 도중에 문제가 발생했어요!"),
    DATA_LOADING_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "데이터를 불러오는데 실패했어요.");

    private final HttpStatus errorCode;
    private final String errorMessage;
}