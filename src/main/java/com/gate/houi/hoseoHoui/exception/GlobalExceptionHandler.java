package com.gate.houi.hoseoHoui.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.gate.houi.hoseoHoui.domain.enumSet.ErrorType;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException e, HttpServletRequest request) {
        return new ResponseEntity<>(
            ErrorResponse.of(e),
            e.getStatus()
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        return new ResponseEntity<>(
            ErrorResponse.of(ErrorType.BAD_REQUEST),
            ErrorType.BAD_REQUEST.getErrorCode()
        );
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException e, HttpServletRequest request) {
        return new ResponseEntity<>(
            ErrorResponse.of(ErrorType.BAD_REQUEST),
            ErrorType.BAD_REQUEST.getErrorCode()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e, HttpServletRequest request) {
        return new ResponseEntity<>(
            ErrorResponse.of(ErrorType.INTERNAL_SERVER_ERROR),
            ErrorType.INTERNAL_SERVER_ERROR.getErrorCode()
        );
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(NoHandlerFoundException e, HttpServletRequest request) {
        return new ResponseEntity<>(
            ErrorResponse.of(ErrorType.BAD_REQUEST),
            ErrorType.BAD_REQUEST.getErrorCode()
        );
    }
}