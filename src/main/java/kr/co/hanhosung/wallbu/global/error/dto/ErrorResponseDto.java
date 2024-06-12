package kr.co.hanhosung.wallbu.global.error.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter(AccessLevel.PROTECTED)
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDto {

    private String errorCode;
    private String message;
}
