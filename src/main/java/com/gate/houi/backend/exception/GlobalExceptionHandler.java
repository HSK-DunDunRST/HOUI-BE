package com.gate.houi.backend.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

        /**
         * BaseException을 처리하는 핸들러
         * BaseException은 사용자 정의 예외로, 특정한 HTTP 상태 코드와 메시지를 가집니다.
         */
        @ExceptionHandler(BaseException.class)
        public ResponseEntity<ErrorResponse> handleBaseException(BaseException baseException, HttpServletRequest request) {
                log.warn("BaseException at {}", request.getRequestURI());
                return ResponseEntity.status(baseException.getStatus()).body(ErrorResponse.of(baseException));
        }

}