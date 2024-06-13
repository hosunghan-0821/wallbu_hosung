package kr.co.hanhosung.wallbu.global.error.dto;


/*
*  Errorcode: (status code) + (application custom error number)
*  ex) NOT_FOUND_EXCEPTION = 404 + 000 = 404000
* */
public enum ErrorCode {

    //NotFound Exception 404/0번대
    NOT_FOUND_EXCEPTION("요청한 리소스를 찾지 못하였습니다.", "404001"),

    //Internal Server Exception 500/0번대
    INTERNAL_SERVER_ERROR("서버 내부 오류", "500001"),
    INTERNAL_SERVER_ERROR_PARSING_ERROR("서버 내부 파싱 오류", "500002"),

    //Invalid Argument Error 관련 400/100번대
    INVALID_ARGUMENT_EXCEPTION("요청하신 데이터에 문제가 있습니다.", "400100"),
    INVALID_ARGUMENT_METHOD_PARAMS("요청하신 데이터의 유효성 검사에 실패하였습니다.", "400101"),

    //BusinessLogic Error 관련 400/200번대
    BUSINESS_LOGIC_EXCEPTION("비지니스 로직에 해당하는 예외입니다.","400200"),
    BUSINESS_LOGIC_EXCEPTION_USER_INFO_INVALID("회원정보가 유효하지 않습니다.","400201"),
    BUSINESS_LOGIC_EXCEPTION_USER_DUPLICATE("이미 가입된 전화번호입니다.","400202"),
    BUSINESS_LOGIC_EXCEPTION_TOKEN_INVALID("토큰이 유효하지 않습니다.","400203"),
    //Authorization Exception 401/0번대
    AUTHORIZATION_EXCEPTION("유효하지 않은 인증입니다.","401001");


    private String defaultMessage;
    private String errorCode;

    ErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    ErrorCode(String defaultMessage, String errorCode) {
        this.defaultMessage = defaultMessage;
        this.errorCode = errorCode;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
