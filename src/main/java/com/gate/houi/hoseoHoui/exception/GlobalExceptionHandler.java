package com.gate.houi.hoseoHoui.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

import com.gate.houi.hoseoHoui.domain.enumType.ErrorType;

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
    public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(NoHandlerFoundException noHandlerFoundException, HttpServletRequest request) {
        return new ResponseEntity<>(
            ErrorResponse.of(ErrorType.BAD_REQUEST),
            ErrorType.BAD_REQUEST.getErrorCode()
        );
    }

    @ExceptionHandler({org.springframework.security.core.AuthenticationException.class})
    public ResponseEntity<ErrorResponse> handleSpringAuthenticationException(AuthenticationException authenticationException) {
        return ResponseEntity
                .status(ErrorType.UNAUTHORIZED_ACCESS.getErrorCode())
                .body(ErrorResponse.of(ErrorType.UNAUTHORIZED_ACCESS));
    }
    
    @ExceptionHandler(OAuth2AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleOAuth2AuthenticationException(OAuth2AuthenticationException oauth2AuthenticationException) {
        return ResponseEntity
                .status(ErrorType.AUTHENTICATION_FAILED.getErrorCode())
                .body(ErrorResponse.of(ErrorType.AUTHENTICATION_FAILED));
    }
}