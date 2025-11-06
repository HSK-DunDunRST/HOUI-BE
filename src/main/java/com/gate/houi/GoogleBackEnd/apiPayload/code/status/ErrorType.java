package com.gate.houi.GoogleBackEnd.apiPayload.code.status;

import com.gate.houi.GoogleBackEnd.apiPayload.code.BaseErrorCode;
import com.gate.houi.GoogleBackEnd.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType implements BaseErrorCode {
    // 에러 예시
    FAIL_OOOOO(HttpStatus.BAD_REQUEST, "FAIL", "실패하였습니다."),

    // 토큰 관련 에러
    TOKEN_FORBIDDEN(HttpStatus.FORBIDDEN, "JWT403", "권한이 없습니다."),
    TOKEN_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "JWT401", "인증이 필요합니다."),
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "JWT401", "유효하지 않은 리프레시 토큰입니다."),
    TOKEN_EMPTY(HttpStatus.UNAUTHORIZED, "JWT401", "JWT 토큰을 넣어주세요."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "JWT401", "만료된 토큰입니다."),
    TOKEN_NOT_MATCHED(HttpStatus.UNAUTHORIZED, "JWT401", "RefreshToken이 일치하지 않습니다."),
    TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "JWT401", "리프레시 토큰이 존재하지 않거나 만료되었습니다."),

    // 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "RESOURCE404", "리소스를 찾을 수 없습니다."),
    MISSING_FILED_ERROR(HttpStatus.NOT_IMPLEMENTED, "FIELD501", "필수 파라미터가 누락되었어요."),

    //로그인 관련
    EMAIL_REGISTERED_WITH_GOOGLE(HttpStatus.BAD_REQUEST, "AUTH4006", "해당 이메일은 구글 조직 계정으로 가입되어 있습니다."),
    GOOGLE_AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "AUTH4007","구글 로그인에 실패했어요."),
    EMAIL_REGISTERED_WITH_LOCAL(HttpStatus.BAD_REQUEST, "AUTH4008", "이미 로컬 계정으로 가입되어 있습니다."),
    LOCAL_LOGIN_FOR_GOOGLE_EMAIL(HttpStatus.UNAUTHORIZED, "USER401", "구글 조직 계정으로 로그인해주세요."),

    // 사용자 관련
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER404", "사용자를 찾을 수 없습니다."),
    USER_INFO_NOT_FOUND(HttpStatus.NOT_FOUND, "USERINFO404", "저장된 사용자 정보가 없습니다."),
    UNAUTHORIZED(HttpStatus.FORBIDDEN, "USERINFO403", "정보 수정 권한이 없습니다."),

    // 공직사항 조회
    NOT_FOUND_NOTICE_DATA(HttpStatus.NOT_FOUND, "NOTICE404", "불러올 공지사항이 없어요."),

    // 서명 응답
    SIGNATURE_INTERNAL_SERVER_ERROR(HttpStatus.BAD_REQUEST, "SIGN400", "서명을 저장하지 못했어요."),
    SIGNATURE_NOT_FOUND(HttpStatus.NOT_FOUND, "SIGN404", "서명이 존재하지 않아요.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}
