package com.gate.houi.backend.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.gate.houi.backend.data.enumType.ErrorType;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

        /**
         * BaseException을 처리하는 핸들러
         * BaseException은 사용자 정의 예외로, 특정한 HTTP 상태 코드와 메시지를 가집니다.
         */
        @ExceptionHandler(BaseException.class)
        public ResponseEntity<ErrorResponse> handleBaseException(BaseException e, HttpServletRequest request) {
                log.warn("BaseException occurred at {}", request.getRequestURI());
                return ResponseEntity.status(e.getStatus()).body(ErrorResponse.of(e));
        }

        /**
         * MethodArgumentNotValidException을 처리하는 핸들러
         * 주로 @Valid 또는 @Validated 어노테이션을 사용한 DTO 검증 실패 시 발생합니다.
         */
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
                log.warn("Validation failed at {}", request.getRequestURI());
                return ResponseEntity.status(ErrorType.INVALID_INPUT_VALUE.getErrorCode()).body(ErrorResponse.of(ErrorType.INVALID_INPUT_VALUE));
        }

        /**
         * BindException을 처리하는 핸들러
         * 주로 @ModelAttribute를 사용한 폼 데이터 바인딩 실패 시 발생합니다.
         */
        @ExceptionHandler(BindException.class)
        public ResponseEntity<ErrorResponse> handleBindException(BindException e, HttpServletRequest request) {
                log.warn("Binding failed at {}", request.getRequestURI());
                return ResponseEntity.status(ErrorType.INVALID_INPUT_VALUE.getErrorCode()).body(ErrorResponse.of(ErrorType.INVALID_INPUT_VALUE));
        }

        /**
         * MethodArgumentTypeMismatchException을 처리하는 핸들러
         * 주로 URL 파라미터 타입이 일치하지 않을 때 발생합니다.
         */
        @ExceptionHandler(MethodArgumentTypeMismatchException.class)
        public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
                log.warn("Type mismatch at {}", request.getRequestURI());
                return ResponseEntity.status(ErrorType.INVALID_INPUT_VALUE.getErrorCode()).body(ErrorResponse.of(ErrorType.INVALID_INPUT_VALUE));
        }

        /**
         * MissingServletRequestParameterException을 처리하는 핸들러
         * 주로 필수 파라미터가 누락되었을 때 발생합니다.
         */
        @ExceptionHandler(MissingServletRequestParameterException.class)
        public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException e, HttpServletRequest request) {
                log.warn("Missing parameter at {}", request.getRequestURI());
                return ResponseEntity.status(ErrorType.MISSING_REQUIRED_FIELDS.getErrorCode()).body(ErrorResponse.of(ErrorType.MISSING_REQUIRED_FIELDS));
        }

        /**
         * HttpRequestMethodNotSupportedException을 처리하는 핸들러
         * 주로 지원하지 않는 HTTP 메소드로 요청했을 때 발생합니다.
         */
        @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
        public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
                log.warn("Method not supported at {}", request.getRequestURI());
                return ResponseEntity.status(ErrorType.METHOD_NOT_ALLOWED.getErrorCode()).body(ErrorResponse.of(ErrorType.METHOD_NOT_ALLOWED));
        }

        /**
         * HttpMessageNotReadableException을 처리하는 핸들러
         * 주로 JSON 파싱 실패 시 발생합니다.
         */
        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
        HttpMessageNotReadableException e, HttpServletRequest request) {
                log.warn("JSON parsing failed at {}", request.getRequestURI());
                return ResponseEntity.status(ErrorType.BAD_REQUEST.getErrorCode()).body(ErrorResponse.of(ErrorType.BAD_REQUEST));
        }

        /**
         * IllegalArgumentException을 처리하는 핸들러
         * 주로 잘못된 인자를 전달했을 때 발생합니다.
         */
        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
                log.warn("IllegalArgumentException at {}", request.getRequestURI());
                return ResponseEntity.status(ErrorType.BAD_REQUEST.getErrorCode()).body(ErrorResponse.of(ErrorType.BAD_REQUEST));
        }

        /**
         * IllegalStateException을 처리하는 핸들러
         * 주로 잘못된 상태에서 메소드를 호출했을 때 발생합니다.
         */
        @ExceptionHandler(IllegalStateException.class)
        public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException e, HttpServletRequest request) {
                log.warn("IllegalStateException at {}", request.getRequestURI());
                return ResponseEntity.status(ErrorType.BAD_REQUEST.getErrorCode()).body(ErrorResponse.of(ErrorType.BAD_REQUEST));
        }

        /**
         * NoHandlerFoundException을 처리하는 핸들러
         * 주로 요청한 URL에 대한 핸들러가 없을 때 발생합니다.
         */
        @ExceptionHandler(NoHandlerFoundException.class)
        public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(NoHandlerFoundException e, HttpServletRequest request) {
                log.warn("No handler found at {}", request.getRequestURI());
                return ResponseEntity.status(ErrorType.NOT_FOUND_ENDPOINT.getErrorCode()).body(ErrorResponse.of(ErrorType.NOT_FOUND_ENDPOINT));
        }

        // /**
        //  * AccessDeniedException을 처리하는 핸들러
        //  * 주로 권한이 없는 리소스에 접근하려고 할 때 발생합니다.
        //  */
        // @ExceptionHandler(AccessDeniedException.class)
        // public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request) {
        //         log.warn("Access denied at {}", request.getRequestURI());
        //         return ResponseEntity.status(ErrorType.ACCESS_DENIED.getErrorCode()).body(ErrorResponse.of(ErrorType.ACCESS_DENIED));
        // }

        // /**
        //  * AuthenticationException을 처리하는 핸들러
        //  * 주로 인증 실패 시 발생합니다.
        //  */
        // @ExceptionHandler(AuthenticationException.class)
        // public ResponseEntity<ErrorResponse> handleSpringAuthenticationException(AuthenticationException e, HttpServletRequest request) {
        //         log.warn("Authentication failed at {}", request.getRequestURI());
        //         return ResponseEntity.status(ErrorType.UNAUTHORIZED_ACCESS.getErrorCode()).body(ErrorResponse.of(ErrorType.UNAUTHORIZED_ACCESS));
        // }

        // /**
        //  * OAuth2AuthenticationException을 처리하는 핸들러
        //  * 주로 OAuth2 인증 실패 시 발생합니다.
        //  */
        // @ExceptionHandler(OAuth2AuthenticationException.class)
        // public ResponseEntity<ErrorResponse> handleOAuth2AuthenticationException(OAuth2AuthenticationException e, HttpServletRequest request) {
        //         log.warn("OAuth2 authentication failed at {}", request.getRequestURI());
        //         return ResponseEntity.status(ErrorType.AUTHENTICATION_FAILED.getErrorCode()).body(ErrorResponse.of(ErrorType.AUTHENTICATION_FAILED));
        // }

        /**
         * Exception을 처리하는 핸들러
         * 모든 예외를 포괄적으로 처리합니다.
         */
        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleException(Exception e, HttpServletRequest request) {
                log.error("Unexpected error occurred at {}", request.getRequestURI());
                return ResponseEntity.status(ErrorType.INTERNAL_SERVER_ERROR.getErrorCode()).body(ErrorResponse.of(ErrorType.INTERNAL_SERVER_ERROR));
        }
}