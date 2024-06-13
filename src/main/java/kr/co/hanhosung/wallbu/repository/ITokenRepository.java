package kr.co.hanhosung.wallbu.repository;

import kr.co.hanhosung.wallbu.domain.RefreshToken;
import kr.co.hanhosung.wallbu.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ITokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findUserByRefreshTokenAndUserId(String refreshToken, long userId);
}
