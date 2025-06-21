package com.gate.houi.hoseoHoui.exception;

import com.gate.houi.hoseoHoui.domain.enumType.ErrorType;

public class BadRequestException extends BaseException {
    public BadRequestException(String message) {
        super(ErrorType.BAD_REQUEST.getErrorCode(),
            ErrorType.BAD_REQUEST.getErrorMessage());
    }
}