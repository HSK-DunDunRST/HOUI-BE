package com.gate.houi.backend.exception;

import com.gate.houi.backend.data.enumType.ErrorType;

/**
 * 사용자를 찾을 수 없을 때 발생하는 예외
 */
public class UserNotFoundException extends BaseException {
    public UserNotFoundException() {
        super(ErrorType.NOT_FOUND_USER.getErrorCode(), 
                ErrorType.NOT_FOUND_USER.getErrorMessage());
    }
}