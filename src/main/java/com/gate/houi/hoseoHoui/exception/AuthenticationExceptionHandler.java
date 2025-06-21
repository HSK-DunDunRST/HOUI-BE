package com.gate.houi.hoseoHoui.exception;

import com.gate.houi.hoseoHoui.domain.enumType.ErrorType;

public class AuthenticationExceptionHandler extends BaseException {
    public AuthenticationExceptionHandler(String message) {
        super(ErrorType.NOT_VALID_TOKEN.getErrorCode(),
            ErrorType.NOT_VALID_TOKEN.getErrorMessage());
    }
}