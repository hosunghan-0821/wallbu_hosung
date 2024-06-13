package kr.co.hanhosung.wallbu.global.util.token;

public interface ITokenManager {

    String createAccessToken(long userId);

    String createRefreshToken();

    void verifyToken(String token);

    long decodeUserId(String token);
}
