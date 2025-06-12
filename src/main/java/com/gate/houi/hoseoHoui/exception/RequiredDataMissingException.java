package com.gate.houi.hoseoHoui.exception;

import com.gate.houi.hoseoHoui.domain.enumSet.ErrorType;

public class RequiredDataMissingException extends BaseException {
    public RequiredDataMissingException() {
        super(ErrorType.MISSING_REQUIRED_FIELDS.getErrorCode(), 
            ErrorType.MISSING_REQUIRED_FIELDS.getErrorMessage());
    }
    
}
