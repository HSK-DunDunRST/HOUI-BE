package com.gate.houi.hoseoHoui.exception;

import com.gate.houi.hoseoHoui.domain.enumType.ErrorType;

public class HandleNoHandlerFoundException extends BaseException {
    public HandleNoHandlerFoundException() {
        super(ErrorType.BAD_REQUEST.getErrorCode(),
            ErrorType.BAD_REQUEST.getErrorMessage());
    }
}
