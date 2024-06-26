package kr.co.hanhosung.wallbu.global.error.exception;


import kr.co.hanhosung.wallbu.global.error.dto.ErrorCode;

public class NotFoundException extends RuntimeException {

    private ErrorCode errorCode;

    public NotFoundException() {
        this.errorCode = ErrorCode.NOT_FOUND_EXCEPTION;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public NotFoundException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

}
