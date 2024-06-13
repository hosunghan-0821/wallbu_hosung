package kr.co.hanhosung.wallbu.global.error.exception;

import kr.co.hanhosung.wallbu.global.error.dto.ErrorCode;

public class AuthorizationException extends RuntimeException {

    private ErrorCode errorCode;

    public AuthorizationException() {
        this.errorCode = ErrorCode.AUTHORIZATION_EXCEPTION;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public AuthorizationException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
