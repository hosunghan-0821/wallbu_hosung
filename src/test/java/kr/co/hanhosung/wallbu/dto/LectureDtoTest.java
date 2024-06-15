package kr.co.hanhosung.wallbu.dto;

import kr.co.hanhosung.wallbu.config.TestConfiguration;
import kr.co.hanhosung.wallbu.global.error.dto.ErrorDetailMessage;
import kr.co.hanhosung.wallbu.global.validation.ValidationMarker;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import javax.validation.ConstraintViolation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@WebMvcTest
@Import({TestConfiguration.class})
class LectureDtoTest {

    @Autowired
    private SpringValidatorAdapter validator;

    @Test
    @DisplayName("[성공] : @Valid 테스트 - OnRegister Lecture")
    public void lectureDtoValidationWithOnRegister() {

        //given
        LectureDto lectureDto = lectureDtoBuilder().build();

        // when
        List<String> messages = getErrorMessagesWithOnRegisterMarker(lectureDto);
        //then
        Assertions.assertThat(messages.size()).isEqualTo(0);
    }
    @Test
    @DisplayName("[실패] : @Valid 테스트 - OnRegister Lecture title null")
    public void lectureDtoTitleValidationWithOnRegister() {

        //given
        LectureDto lectureDto = lectureDtoBuilder().title(null).build();

        // when
        List<String> messages = getErrorMessagesWithOnRegisterMarker(lectureDto);
        //then
        Assertions.assertThat(messages).contains(ErrorDetailMessage.NOT_NULL);
    }
    @Test
    @DisplayName("[실패] : @Valid 테스트 - OnRegister Lecture maxStudnetCount null")
    public void lectureDtoMaxStudentCountValidationWithOnRegister() {

        //given
        LectureDto lectureDto = lectureDtoBuilder().maxStudentCount(null).build();

        // when
        List<String> messages = getErrorMessagesWithOnRegisterMarker(lectureDto);
        //then
        Assertions.assertThat(messages).contains(ErrorDetailMessage.NOT_NULL);
    }
    @Test
    @DisplayName("[실패] : @Valid 테스트 - OnRegister Lecture price null")
    public void lectureDtoPriceValidationWithOnRegister() {

        //given
        LectureDto lectureDto = lectureDtoBuilder().price(null).build();

        // when
        List<String> messages = getErrorMessagesWithOnRegisterMarker(lectureDto);
        //then
        Assertions.assertThat(messages).contains(ErrorDetailMessage.NOT_NULL);
    }

    @Test
    @DisplayName("[성공] : @Valid 테스트 - OnEnroll Lecture")
    public void lectureDtoValidationWithOnEnroll() {

        //given
        LectureDto lectureDto = lectureDtoBuilder().id(1L).build();

        // when
        List<String> messages = getErrorMessagesWithOnEnrollMarker(lectureDto);
        //then
        Assertions.assertThat(messages.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("[실패] : @Valid 테스트 - OnEnroll Lecture id null")
    public void lectureDtoValidationWithOnEnrollFail() {

        //given
        LectureDto lectureDto = lectureDtoBuilder().id(null).build();

        // when
        List<String> messages = getErrorMessagesWithOnEnrollMarker(lectureDto);
        //then
        Assertions.assertThat(messages).contains(ErrorDetailMessage.NOT_NULL);
    }



    private List<String> getErrorMessagesWithOnRegisterMarker(LectureDto lectureDto) {
        Set<ConstraintViolation<LectureDto>> validate = validator.validate(lectureDto, Errors.class, ValidationMarker.OnRegisterLecture.class);

        return getMessages(validate);
    }

    private List<String> getErrorMessagesWithOnEnrollMarker(LectureDto lectureDto) {
        Set<ConstraintViolation<LectureDto>> validate = validator.validate(lectureDto, Errors.class, ValidationMarker.OnEnrollLecture.class);

        return getMessages(validate);
    }

    private static List<String> getMessages(Set<ConstraintViolation<LectureDto>> validate) {
        Iterator<ConstraintViolation<LectureDto>> iterator = validate.iterator();
        List<String> messages = new ArrayList<>();
        while (iterator.hasNext()) {
            ConstraintViolation<LectureDto> next = iterator.next();
            messages.add(next.getMessage());
            System.out.println("message = " + next.getMessage());
        }
        return messages;
    }


    private LectureDto.LectureDtoBuilder lectureDtoBuilder() {

        return LectureDto.builder()
                .title("hi")
                .maxStudentCount(10L)
                .price(1_000L);


    }
}