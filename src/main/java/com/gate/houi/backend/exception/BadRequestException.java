package com.gate.houi.backend.exception;

import com.gate.houi.backend.data.enumType.ErrorType;

/**
 * 잘못된 요청일 때 발생하는 예외
 */
public class BadRequestException extends BaseException {
    public BadRequestException() {
        super(ErrorType.BAD_REQUEST.getErrorCode(),
                ErrorType.BAD_REQUEST.getErrorMessage());
    }
}