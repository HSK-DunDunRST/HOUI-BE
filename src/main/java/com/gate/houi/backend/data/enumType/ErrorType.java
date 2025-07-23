package com.gate.houi.backend.data.enumType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorType {
    
    // Error Code - 400
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청이에요."),
    MISSING_REQUIRED_FIELDS(HttpStatus.BAD_REQUEST, "입력 항목을 다시 확인해주세요."),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "입력값이 유효하지 않아요."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 HTTP 메서드예요."),
    
    // Error code - 401
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "인증되지 않은 접근이에요. 로그인이 필요해요."),
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "인증 처리 중 오류가 발생했어요."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰이 만료되었어요."),
    LOGOUT_FAILED(HttpStatus.UNAUTHORIZED, "로그아웃 처리 중 오류가 발생했어요."),
    
    // Error code - 200 (No data available)
    NO_NOTICE_AVAILABLE(HttpStatus.OK, "등록된 공지사항이 없어요."),
    
    // Error code - 403
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없어요."),
    
    // Error code - 404
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "학생정보를 찾을 수 없어요."),
    NOT_FOUND_REQUEST_DATA(HttpStatus.NOT_FOUND, "요청한 데이터를 찾을 수 없어요."),
    NOT_FOUND_ENDPOINT(HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없어요."),
    
    // Error code - 500
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "요청을 처리하는 도중에 문제가 발생했어요!");

    private final HttpStatus errorCode;
    private final String errorMessage;
}