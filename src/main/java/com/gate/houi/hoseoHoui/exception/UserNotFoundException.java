package com.gate.houi.hoseoHoui.exception;

import com.gate.houi.hoseoHoui.domain.enumSet.ErrorType;

public class UserNotFoundException extends BaseException {
    public UserNotFoundException() {
        super(ErrorType.NOT_FOUND_USER.getErrorCode(), ErrorType.NOT_FOUND_USER.getErrorMessage());
    }
}