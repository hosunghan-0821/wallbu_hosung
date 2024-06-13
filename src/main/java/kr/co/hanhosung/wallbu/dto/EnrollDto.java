package kr.co.hanhosung.wallbu.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import java.util.List;

@Getter
@Setter(AccessLevel.PROTECTED)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnrollDto {

    @Valid
    private List<LectureDto> lectureList;


}
