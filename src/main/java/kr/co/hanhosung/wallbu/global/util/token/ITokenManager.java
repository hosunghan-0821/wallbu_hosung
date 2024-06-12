package kr.co.hanhosung.wallbu.global.util.token;

public interface ITokenManager {

    String createAccessToken(long userId, String username);

    String createRefreshToken();
}
