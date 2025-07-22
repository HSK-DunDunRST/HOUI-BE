package com.gate.houi.backend.exception;

import com.gate.houi.backend.data.enumType.ErrorType;

public class NoticeDataNotFoundException extends BaseException {
    public NoticeDataNotFoundException() {
        super(ErrorType.NOT_FOUND_NOTICE.getErrorCode(),
                ErrorType.NOT_FOUND_NOTICE.getErrorMessage());
    }
}