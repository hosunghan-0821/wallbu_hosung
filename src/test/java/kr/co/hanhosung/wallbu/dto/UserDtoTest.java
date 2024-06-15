package kr.co.hanhosung.wallbu.dto;

import kr.co.hanhosung.wallbu.config.MvcTestConfiguration;
import kr.co.hanhosung.wallbu.domain.UserRole;
import kr.co.hanhosung.wallbu.global.error.dto.ErrorDetailMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@WebMvcTest
@Import({MvcTestConfiguration.class})
class UserDtoTest {

    @Autowired
    private Validator validatorInjected;


    @Test
    @DisplayName("[실패] : @Valid 테스트 - UserRome 널 체크")
    public void signUpUserROleValidationTestNull() {

        //given
        UserDto userDto = userDtoBuilder().userRole(null).build();

        // when
        List<String> messages = getErrorMessages(userDto);
        //then
        Assertions.assertThat(messages).contains(ErrorDetailMessage.NOT_NULL);
    }

    @Test
    @DisplayName("[실패] : @Valid 테스트 - phoneNumber 널 체크")
    public void signUpPhoneNumberValidationTestNull() {

        //given
        UserDto userDto = userDtoBuilder().phoneNumber(null).build();

        // when
        List<String> messages = getErrorMessages(userDto);
        //then
        Assertions.assertThat(messages).contains(ErrorDetailMessage.NOT_NULL);
    }

    @Test
    @DisplayName("[실패] : @Valid 테스트 - email 널 체크")
    public void signUpEmailValidationTestNull() {

        //given
        UserDto userDto = userDtoBuilder().email(null).build();

        // when
        List<String> messages = getErrorMessages(userDto);
        //then
        Assertions.assertThat(messages).contains(ErrorDetailMessage.NOT_NULL);
    }

    @Test
    @DisplayName("[실패] : @Valid 테스트 - 이름 널 체크")
    public void signUpNamedValidationTestNull() {

        //given
        UserDto userDto = userDtoBuilder().name(null).build();

        // when
        List<String> messages = getErrorMessages(userDto);
        //then
        Assertions.assertThat(messages).contains(ErrorDetailMessage.NOT_NULL);
    }

    @Test
    @DisplayName("[실패] : @Valid 테스트 - 비밀번호 길이")
    public void signUpPasswordValidationTestLength1() {

        //given
        UserDto userDto = userDtoBuilder().password("123hh").build();

        // when
        List<String> messages = getErrorMessages(userDto);
        //then
        Assertions.assertThat(messages).contains(ErrorDetailMessage.INVALID_PASSWORD);
    }

    @Test
    @DisplayName("[실패] : @Valid 테스트 - 비밀번호 길이")
    public void signUpPasswordValidationTestLength2() {

        //given
        UserDto userDto = userDtoBuilder().password("h1234567890").build();

        // when
        List<String> messages = getErrorMessages(userDto);
        //then
        Assertions.assertThat(messages).contains(ErrorDetailMessage.INVALID_PASSWORD);
    }

    @Test
    @DisplayName("[실패] : @Valid 테스트 - 비밀번호 조합")
    public void signUpPasswordValidationTestCombination1() {

        //given
        UserDto userDto = userDtoBuilder().password("hhhhhh").build();
        // when
        List<String> messages = getErrorMessages(userDto);
        //then
        Assertions.assertThat(messages).contains(ErrorDetailMessage.INVALID_PASSWORD);
    }

    @Test
    @DisplayName("[실패] : @Valid 테스트 - 비밀번호 조합2")
    public void signUpPasswordValidationTestCombination2() {

        //given
        UserDto userDto = userDtoBuilder().password("222222").build();

        // when
        List<String> messages = getErrorMessages(userDto);
        //then
        Assertions.assertThat(messages).contains(ErrorDetailMessage.INVALID_PASSWORD);
    }

    @Test
    @DisplayName("[실패] : @Valid 테스트 - 비밀번호 조합3")
    public void signUpPasswordValidationTestCombination3() {

        //given
        UserDto userDto = userDtoBuilder().password("HHHHHH").build();
        // when
        List<String> messages = getErrorMessages(userDto);
        //then
        Assertions.assertThat(messages).contains(ErrorDetailMessage.INVALID_PASSWORD);
    }

    @Test
    @DisplayName("[성공] : @Valid 테스트 - 비밀번호 조합3")
    public void signUpPasswordValidation() {

        //given
        UserDto userDto = userDtoBuilder().password("h123456").build();
        // when
        List<String> messages = getErrorMessages(userDto);
        //then
        Assertions.assertThat(messages.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("[성공] : @Valid 테스트 - 비밀번호 조합2")
    public void signUpPasswordValidation2() {

        //given
        UserDto userDto = userDtoBuilder().password("H123456").build();
        // when
        List<String> messages = getErrorMessages(userDto);
        //then
        Assertions.assertThat(messages.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("[성공] : @Valid 테스트 - 비밀번호 조합1")
    public void signUpPasswordValidation3() {

        //given
        UserDto userDto = userDtoBuilder().password("hhhHHH").build();
        // when
        List<String> messages = getErrorMessages(userDto);
        //then
        Assertions.assertThat(messages.size()).isEqualTo(0);
    }



    private List<String> getErrorMessages(UserDto userDto) {
        Set<ConstraintViolation<UserDto>> validate = validatorInjected.validate(userDto);

        Iterator<ConstraintViolation<UserDto>> iterator = validate.iterator();
        List<String> messages = new ArrayList<>();
        while (iterator.hasNext()) {
            ConstraintViolation<UserDto> next = iterator.next();
            messages.add(next.getMessage());
            System.out.println("message = " + next.getMessage());
        }
        return messages;
    }


    private UserDto.UserDtoBuilder userDtoBuilder() {

        return UserDto.builder()
                .userRole(UserRole.STUDENT)
                .name("한호성")
                .phoneNumber("01012345678")
                .password("ghtjd114")
                .email("winsomed96@naver.com");
    }

}