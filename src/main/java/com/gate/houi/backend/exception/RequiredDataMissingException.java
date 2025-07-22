package com.gate.houi.backend.exception;

import com.gate.houi.backend.data.enumType.ErrorType;

/**
 * 필수 데이터가 누락되었을 때 발생하는 예외
 */
public class RequiredDataMissingException extends BaseException {
    public RequiredDataMissingException() {
        super(ErrorType.MISSING_REQUIRED_FIELDS.getErrorCode(), 
                ErrorType.MISSING_REQUIRED_FIELDS.getErrorMessage());
    }
}
