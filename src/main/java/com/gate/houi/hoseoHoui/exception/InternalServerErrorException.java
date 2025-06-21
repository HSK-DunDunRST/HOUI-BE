package com.gate.houi.hoseoHoui.exception;

import com.gate.houi.hoseoHoui.domain.enumType.ErrorType;

public class InternalServerErrorException extends BaseException {
    public InternalServerErrorException() {
        super(ErrorType.INTERNAL_SERVER_ERROR.getErrorCode(),
            ErrorType.INTERNAL_SERVER_ERROR.getErrorMessage());
    }
}