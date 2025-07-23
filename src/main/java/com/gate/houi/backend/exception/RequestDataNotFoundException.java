package com.gate.houi.backend.exception;

import com.gate.houi.backend.data.enumType.ErrorType;

public class RequestDataNotFoundException extends BaseException {
    public RequestDataNotFoundException() {
        super(ErrorType.NOT_FOUND_REQUEST_DATA.getErrorCode(), ErrorType.NOT_FOUND_REQUEST_DATA.getErrorMessage());
    }

}
