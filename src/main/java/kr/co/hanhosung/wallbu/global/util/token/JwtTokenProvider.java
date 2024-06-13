package kr.co.hanhosung.wallbu.global.util.token;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import kr.co.hanhosung.wallbu.global.error.dto.ErrorCode;
import kr.co.hanhosung.wallbu.global.error.exception.AuthorizationException;
import kr.co.hanhosung.wallbu.global.error.exception.BusinessLogicException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
@Slf4j
public class JwtTokenProvider implements ITokenManager {

    public static final String JWT_CLAIM_USER_KEY = "userId";
    public static final String JWT_CLAIM_USER_NAME_KEY = "name";

    private final JwtDecoder jwtDecoder;
    private long accessTokenValidTime = 60000 * 30; //30분

    private long refreshTokenValidTime = 60000 * 60 * 24 * 14; //2주

    private Algorithm algorithm;
    private String secretKey;


    @Autowired
    public JwtTokenProvider(@Value("${jwt.secret-key}") String secretKey, JwtDecoder jwtDecoder) {

        assert (secretKey != null && !secretKey.isEmpty());

        this.secretKey = secretKey;
        this.algorithm = Algorithm.HMAC256(secretKey);
        this.jwtDecoder = jwtDecoder;
    }

    public JwtTokenProvider(@Value("${jwt.secret-key}") String secretKey, long accessTokenValidTime, long refreshTokenValidTime, JwtDecoder jwtDecoder) {

        assert (secretKey != null && !secretKey.isEmpty());

        this.secretKey = secretKey;
        this.algorithm = Algorithm.HMAC256(secretKey);
        this.jwtDecoder = jwtDecoder;
        this.accessTokenValidTime = accessTokenValidTime;
        this.refreshTokenValidTime = refreshTokenValidTime;
    }

    @Override
    public String createAccessToken(long userId) {

        assert (userId > 0);

        String token = JWT.create()
                .withClaim(JWT_CLAIM_USER_KEY, userId)
                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenValidTime))
                .sign(algorithm);
        return token;

    }

    @Override
    public String createRefreshToken() {

        String token = JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshTokenValidTime))
                .sign(algorithm);
        return token;
    }

    @Override
    public void verifyToken(String token) {

        assert (token != null);
        token = removePrefix(token);
        DecodedJWT decodedTokenOrNull = jwtDecoder.getDecodedTokenOrNull(token);
        if (decodedTokenOrNull == null) {
            throw new AuthorizationException();
        }

    }

    @Override
    public long decodeUserId(String token) {
        token = removePrefix(token);
        DecodedJWT decodedTokenOrNull = JWT.decode(token);

        return decodedTokenOrNull.getClaim(JWT_CLAIM_USER_KEY).asLong();
    }

    private String removePrefix(String token) {
        if (token.startsWith("Bearer ")) {
            return token.substring(7);
        } else {
            return token;
        }
    }


}
