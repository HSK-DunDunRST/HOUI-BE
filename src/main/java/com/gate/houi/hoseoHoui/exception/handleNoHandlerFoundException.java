package com.gate.houi.hoseoHoui.exception;

import com.gate.houi.hoseoHoui.domain.enumSet.ErrorType;

public class handleNoHandlerFoundException extends BaseException {
    public handleNoHandlerFoundException() {
        super(ErrorType.BAD_REQUEST.getErrorCode(),
            ErrorType.BAD_REQUEST.getErrorMessage());
    }
}
