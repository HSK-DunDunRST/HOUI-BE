package com.gate.houi.hoseoHoui.exception;

import com.gate.houi.hoseoHoui.domain.enumType.ErrorType;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

@Getter
@Builder
@RequiredArgsConstructor
public class ErrorResponse {

    private final int status;
    private final String error;
    private final LocalDateTime timestamp;

    public static ErrorResponse of(BaseException e) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(e.getStatus().value())
                .error(e.getMessage())
                .build();
    }

    public static ErrorResponse of(ErrorType errorType){
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(errorType.getErrorCode().value())
                .error(errorType.getErrorMessage())
                .build();
    }

    public static ErrorResponse of(Exception exception, HttpStatus httpStatus){
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(httpStatus.value())
                .error(exception.getMessage())
                .build();
    }
}
