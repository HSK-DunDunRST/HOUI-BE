package com.gate.houi.hoseoHoui.exception;

import com.gate.houi.hoseoHoui.domain.enumType.ErrorType;

public class UserNotFoundException extends BaseException {
    public UserNotFoundException() {
        super(ErrorType.NOT_FOUND_USER.getErrorCode(), ErrorType.NOT_FOUND_USER.getErrorMessage());
    }
    public UserNotFoundException(String message) {
        super(ErrorType.NOT_FOUND_USER.getErrorCode(), message);
    }
}