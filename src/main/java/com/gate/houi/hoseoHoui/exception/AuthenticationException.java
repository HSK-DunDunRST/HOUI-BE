package com.gate.houi.hoseoHoui.exception;

import com.gate.houi.hoseoHoui.domain.enumType.ErrorType;

public class AuthenticationException extends BaseException {
    
    public AuthenticationException() {
        super(ErrorType.UNAUTHORIZED_ACCESS.getErrorCode(),
            ErrorType.UNAUTHORIZED_ACCESS.getErrorMessage());
    }
    
    public AuthenticationException(String message) {
        super(ErrorType.UNAUTHORIZED_ACCESS.getErrorCode(),
            message != null ? message : ErrorType.UNAUTHORIZED_ACCESS.getErrorMessage());
    }
}