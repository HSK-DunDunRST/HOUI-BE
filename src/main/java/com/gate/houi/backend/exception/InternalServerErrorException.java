package com.gate.houi.backend.exception;

import com.gate.houi.backend.data.enumType.ErrorType;

/**
 * 내부 서버 오류가 발생했을 때 던지는 예외
 */
public class InternalServerErrorException extends BaseException {
    public InternalServerErrorException() {
        super(ErrorType.INTERNAL_SERVER_ERROR.getErrorCode(),
                ErrorType.INTERNAL_SERVER_ERROR.getErrorMessage());
    }
}