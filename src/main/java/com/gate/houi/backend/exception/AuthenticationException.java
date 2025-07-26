package com.gate.houi.backend.exception;

import com.gate.houi.backend.data.enumType.ErrorType;

/**
 * 인증에 실패했을 때 발생하는 예외
 */
public class AuthenticationException extends BaseException {
    
    public AuthenticationException() {
        super(ErrorType.UNAUTHORIZED_ACCESS.getErrorCode(), ErrorType.UNAUTHORIZED_ACCESS.getErrorMessage());
    }
}