package kr.co.hanhosung.wallbu.dto;

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
public class TokenDto {

    private String accessToken;
    private String refreshToken;

    public TokenDto(String accessToken, String refreshToken) {
        assert (accessToken!= null);
        assert (refreshToken != null);

        if(accessToken.startsWith("Bearer ")) {
            accessToken = accessToken.substring(7);
        }
        if(refreshToken.startsWith("Bearer ")) {
            refreshToken = refreshToken.substring(7);
        }


        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
