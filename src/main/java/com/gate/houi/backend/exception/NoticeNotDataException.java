package com.gate.houi.backend.exception;

import com.gate.houi.backend.data.enumType.ErrorType;

public class NoticeNotDataException extends BaseException {
    public NoticeNotDataException() {
        super(ErrorType.NO_NOTICE_AVAILABLE.getErrorCode(),
                ErrorType.NO_NOTICE_AVAILABLE.getErrorMessage());
    }
}