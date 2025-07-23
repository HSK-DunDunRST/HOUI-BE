package com.gate.houi.backend.exception;

import com.gate.houi.backend.data.enumType.ErrorType;

public class NoticeDataNotFoundException extends BaseException {
    public NoticeDataNotFoundException() {
        super(ErrorType.NO_NOTICE_AVAILABLE.getErrorCode(),
                ErrorType.NO_NOTICE_AVAILABLE.getErrorMessage());
    }
}