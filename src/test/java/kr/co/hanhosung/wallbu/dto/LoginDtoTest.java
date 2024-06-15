package kr.co.hanhosung.wallbu.dto;

import kr.co.hanhosung.wallbu.config.TestConfiguration;
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
@Import({TestConfiguration.class})
class LoginDtoTest {

    @Autowired
    private Validator validatorInjected;


    @Test
    @DisplayName("[성공] : @Valid 테스트")
    public void loginPasswordValidation() {

        //given
        LoginDto loginDto = loginDtoBuilder().build();

        // when
        List<String> messages = getErrorMessages(loginDto);
        //then
        Assertions.assertThat(messages.size()).isEqualTo(0);
    }




    @Test
    @DisplayName("[실패] : @Valid 테스트 - password 널 체크")
    public void loginPasswordValidationTestNull() {

        //given
        LoginDto loginDto = loginDtoBuilder().password(null).build();

        // when
        List<String> messages = getErrorMessages(loginDto);
        //then
        Assertions.assertThat(messages).contains(ErrorDetailMessage.NOT_NULL);
    }


    @Test
    @DisplayName("[실패] : @Valid 테스트 - id 널 체크")
    public void loginIdValidationTestNull() {

        //given
        LoginDto loginDto = loginDtoBuilder().id(null).build();

        // when
        List<String> messages = getErrorMessages(loginDto);
        //then
        Assertions.assertThat(messages).contains(ErrorDetailMessage.NOT_NULL);
    }


    private List<String> getErrorMessages(LoginDto loginDto) {
        Set<ConstraintViolation<LoginDto>> validate = validatorInjected.validate(loginDto);

        Iterator<ConstraintViolation<LoginDto>> iterator = validate.iterator();
        List<String> messages = new ArrayList<>();
        while (iterator.hasNext()) {
            ConstraintViolation<LoginDto> next = iterator.next();
            messages.add(next.getMessage());
            System.out.println("message = " + next.getMessage());
        }
        return messages;
    }


    private LoginDto.LoginDtoBuilder loginDtoBuilder() {

        return LoginDto.builder()
                .id("01095756302")
                .password("hosung114");
    }
}