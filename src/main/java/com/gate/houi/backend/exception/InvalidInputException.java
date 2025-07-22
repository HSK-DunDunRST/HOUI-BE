package com.gate.houi.backend.exception;

import com.gate.houi.backend.data.enumType.ErrorType;

/**
 * 입력값이 유효하지 않을 때 발생하는 예외
 */
public class InvalidInputException extends BaseException {
    public InvalidInputException() {
        super(ErrorType.INVALID_INPUT_VALUE.getErrorCode(), 
                ErrorType.INVALID_INPUT_VALUE.getErrorMessage());
    }
}
