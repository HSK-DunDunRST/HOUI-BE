package com.gate.houi.backend.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import com.gate.houi.backend.data.enumType.ErrorType;

@Slf4j
@Getter
public class BaseException extends RuntimeException {
    private final HttpStatus status;
    private final String message;

    public BaseException(HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.message = message;

        log.warn("Error : [{}] {}", status, message);
    }

    public BaseException(ErrorType errorType) {
        super(errorType.getErrorMessage());
        this.status = errorType.getErrorCode();
        this.message = errorType.getErrorMessage();

        log.warn("Error : [{}] {}", status, message);
    }
}