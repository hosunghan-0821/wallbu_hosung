package kr.co.hanhosung.wallbu.repository;

import kr.co.hanhosung.wallbu.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByPhoneNumber(String phoneNumber);


}
