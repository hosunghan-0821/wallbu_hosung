package kr.co.hanhosung.wallbu.global.util.token;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import kr.co.hanhosung.wallbu.global.error.exception.AuthorizationException;
import kr.co.hanhosung.wallbu.global.error.exception.BusinessLogicException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class JwtTokenProviderTest {


    private static final String WALLBU_HOSUNG = "wallbuhosung@";
    private static final String WALLBU_HOSUNG_ANOTHER = "wallbuhosung@222";

    @Test
    @DisplayName("[성공] : AccessToken 생성 테스트")
    void createAccessToken() {
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(WALLBU_HOSUNG, new JwtDecoder(WALLBU_HOSUNG));
        String accessToken = jwtTokenProvider.createAccessToken(1L);
        Assertions.assertNotNull(accessToken);
    }

    @Test
    @DisplayName("[성공] : RefreshToken 생성 테스트")
    void createRefreshToken() {
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(WALLBU_HOSUNG, new JwtDecoder(WALLBU_HOSUNG));
        String refreshToken = jwtTokenProvider.createRefreshToken();
        Assertions.assertNotNull(refreshToken);
    }


    @Test
    @DisplayName("[성공] : Token verify 테스트")
    void verifyToken() {

        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(WALLBU_HOSUNG, new JwtDecoder(WALLBU_HOSUNG));
        String accessToken = jwtTokenProvider.createAccessToken(1L);
        jwtTokenProvider.verifyToken(accessToken);

    }

    @Test
    @DisplayName("[성공] : Token 내부 데이터 조회")
    void getUserIdByToken() {

        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(WALLBU_HOSUNG, new JwtDecoder(WALLBU_HOSUNG));
        String accessToken = jwtTokenProvider.createAccessToken(1L);
        jwtTokenProvider.verifyToken(accessToken);
        long userId = jwtTokenProvider.decodeUserId(accessToken);

        Assertions.assertEquals(1L, userId);

    }


    @Test
    @DisplayName("[실패] : Token verify 생성 테스트 - secretKey 다름")
    void verifyTokenFail() {

        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(WALLBU_HOSUNG, new JwtDecoder(WALLBU_HOSUNG));
        String accessToken = jwtTokenProvider.createAccessToken(1L);

        JwtTokenProvider jwtTokenProviderAnother = new JwtTokenProvider(WALLBU_HOSUNG_ANOTHER, new JwtDecoder(WALLBU_HOSUNG_ANOTHER));
        Assertions.assertThrows(AuthorizationException.class, () -> jwtTokenProviderAnother.verifyToken(accessToken));
    }


    @Test
    @DisplayName("[실패] : Token verify 생성 테스트 - 기간 만료")
    void verifyTokenFail2() {

        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(WALLBU_HOSUNG, -1000L, 0L, new JwtDecoder(WALLBU_HOSUNG));
        String accessToken = jwtTokenProvider.createAccessToken(1L);

        Assertions.assertThrows(AuthorizationException.class, () -> jwtTokenProvider.verifyToken(accessToken));

    }
}