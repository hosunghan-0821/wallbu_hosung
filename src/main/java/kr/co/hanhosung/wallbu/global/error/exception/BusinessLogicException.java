package kr.co.hanhosung.wallbu.global.error.exception;


import kr.co.hanhosung.wallbu.global.error.dto.ErrorCode;

public class BusinessLogicException  extends RuntimeException{

    private ErrorCode errorCode;

    public BusinessLogicException() {
        this.errorCode = ErrorCode.BUSINESS_LOGIC_EXCEPTION;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public BusinessLogicException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
