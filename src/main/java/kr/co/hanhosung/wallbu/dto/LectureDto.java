package kr.co.hanhosung.wallbu.dto;

import kr.co.hanhosung.wallbu.domain.Lecture;
import kr.co.hanhosung.wallbu.domain.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

import static kr.co.hanhosung.wallbu.global.error.dto.ErrorDetailMessage.NOT_NULL;
import static kr.co.hanhosung.wallbu.global.validation.ValidationMarker.*;

@Getter
@Setter(AccessLevel.PROTECTED)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LectureDto {

    @NotNull(groups = {OnEnrollLecture.class}, message = NOT_NULL)
    private Long id;

    @NotNull(groups = {OnRegisterLecture.class}, message = NOT_NULL)
    private String title;

    @NotNull(groups = {OnRegisterLecture.class}, message = NOT_NULL)
    private Long maxStudentCount;

    @NotNull(groups = {OnRegisterLecture.class}, message = NOT_NULL)
    private Long price;

    private Long studentCount;

    public Lecture toLectureEntity(User createUser) {

        assert (title != null && maxStudentCount != null && price != null);

        return new Lecture(this.title, this.maxStudentCount, this.price, createUser);
    }
}
