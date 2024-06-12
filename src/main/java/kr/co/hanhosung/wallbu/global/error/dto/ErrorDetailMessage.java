package kr.co.hanhosung.wallbu.global.error.dto;

public class ErrorDetailMessage {

    public static final String NULL ="널 값이어야 합니다.";
    public static final String NOT_NULL="널을 허용하지 않습니다.";

    public static final String INVALID_PASSWORD = "최소 6자 이상 10자 이하 영문 소문자, 대문자, 숫자 중 최소 두 가지 이상 조합 필요합니다.";

    //Instance 생성 방지
    private ErrorDetailMessage(){

    }
}
