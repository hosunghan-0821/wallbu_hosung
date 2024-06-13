package kr.co.hanhosung.wallbu.global.util.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class JwtDecoder {


    private String secretKey;

    public JwtDecoder(@Value("${jwt.secret-key}") String secretKey) {

        assert (secretKey != null && !secretKey.isEmpty());

        this.secretKey = secretKey;
    }


    public DecodedJWT getDecodedTokenOrNull(String token) {

        DecodedJWT decodedJwt = null;
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            JWTVerifier verifier = JWT
                    .require(algorithm)
                    .build();
            decodedJwt = verifier.verify(token);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return decodedJwt;


    }
}
