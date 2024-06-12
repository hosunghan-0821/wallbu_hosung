package kr.co.hanhosung.wallbu.dto;

import kr.co.hanhosung.wallbu.domain.User;
import kr.co.hanhosung.wallbu.domain.UserRole;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PROTECTED)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;

    private String name;

    private String email;

    private String phoneNumber;

    private String password;

    private UserRole userRole;


    public User toUserEntity() {

        assert (this.name != null && !this.name.isEmpty());
        assert (this.email != null && !this.email.isEmpty());
        assert (this.phoneNumber != null && !this.phoneNumber.isEmpty());
        assert (this.password != null && !this.password.isEmpty());

        return new User(this.name, this.email, this.phoneNumber, this.password, this.userRole);
    }

    public void updatePasswordByEncrypt(String password) {
        this.password = password;
    }

}
