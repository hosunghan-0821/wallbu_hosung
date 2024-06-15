package kr.co.hanhosung.wallbu.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import kr.co.hanhosung.wallbu.domain.User;
import kr.co.hanhosung.wallbu.domain.UserRole;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static kr.co.hanhosung.wallbu.global.error.dto.ErrorDetailMessage.INVALID_PASSWORD;
import static kr.co.hanhosung.wallbu.global.error.dto.ErrorDetailMessage.NOT_NULL;

@Getter
@Setter(AccessLevel.PROTECTED)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;


    @NotNull(message = NOT_NULL)
    private String name;

    @Email
    @NotNull(message = NOT_NULL)
    private String email;

    @NotNull(message = NOT_NULL)
    private String phoneNumber;

    @Pattern(regexp = "^(?!((?:[A-Z]+)|(?:[a-z]+)|(?:[0-9]+))$)[A-Za-z\\d]{6,10}$", message = INVALID_PASSWORD)
    @NotNull(message = NOT_NULL)
    private String password;

    @NotNull(message = NOT_NULL)
    private UserRole userRole;


    public static UserDto toUserDto(User user) {
        UserDto userDto = UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .userRole(user.getUserRole())
                .build();

        assert (userDto.getPassword() == null);

        return userDto;
    }

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
