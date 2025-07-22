package com.gate.houi.backend.exception;

import com.gate.houi.backend.data.enumType.ErrorType;

public class HandleNoHandlerFoundException extends BaseException {
    public HandleNoHandlerFoundException() {
        super(ErrorType.BAD_REQUEST.getErrorCode(),
                ErrorType.BAD_REQUEST.getErrorMessage());
    }
}
