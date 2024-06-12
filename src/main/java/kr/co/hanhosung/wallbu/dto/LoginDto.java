package kr.co.hanhosung.wallbu.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

import static kr.co.hanhosung.wallbu.global.error.dto.ErrorDetailMessage.NOT_NULL;

@Getter
@Setter(AccessLevel.PROTECTED)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {

    @NotNull(message = NOT_NULL)
    private String id;

    @NotNull(message = NOT_NULL)
    private String password;

}
