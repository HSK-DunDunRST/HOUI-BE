package com.gate.houi.backend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import com.gate.houi.backend.data.enumType.ErrorType;

@Getter
public class BaseException extends RuntimeException {
    private final HttpStatus status;
    private final String message;

    public BaseException(HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }

    public BaseException(ErrorType errorType) {
        super(errorType.getErrorMessage());
        this.status = errorType.getErrorCode();
        this.message = errorType.getErrorMessage();
    }
}