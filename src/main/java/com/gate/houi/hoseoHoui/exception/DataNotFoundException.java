package com.gate.houi.hoseoHoui.exception;

import com.gate.houi.hoseoHoui.domain.enumSet.ErrorType;

public class DataNotFoundException extends BaseException {
    public DataNotFoundException() {
        super(ErrorType.NOT_FOUND_DATA.getErrorCode(),
            ErrorType.NOT_FOUND_DATA.getErrorMessage());
    }

    public DataNotFoundException(String message) {
        super(ErrorType.NOT_FOUND_DATA.getErrorCode(),
            message != null ? message : ErrorType.NOT_FOUND_DATA.getErrorMessage());
    }
}