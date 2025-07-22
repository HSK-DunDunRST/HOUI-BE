package com.gate.houi.backend.exception;

import com.gate.houi.backend.data.enumType.ErrorType;

/**
 * 토큰이 만료되었을 때 발생하는 예외
 */
public class TokenExpiredException extends BaseException {
    
    public TokenExpiredException() {
        super(ErrorType.TOKEN_EXPIRED.getErrorCode(), 
                ErrorType.TOKEN_EXPIRED.getErrorMessage());
    }
}
