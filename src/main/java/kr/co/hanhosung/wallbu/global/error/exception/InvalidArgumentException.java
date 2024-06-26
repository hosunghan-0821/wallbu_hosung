package kr.co.hanhosung.wallbu.global.error.exception;


import kr.co.hanhosung.wallbu.global.error.dto.ErrorCode;

public class InvalidArgumentException extends RuntimeException{

    private ErrorCode errorCode;

    public InvalidArgumentException() {
        this.errorCode = ErrorCode.INVALID_ARGUMENT_EXCEPTION;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public InvalidArgumentException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
