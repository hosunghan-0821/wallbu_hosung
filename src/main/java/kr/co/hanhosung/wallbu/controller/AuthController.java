package kr.co.hanhosung.wallbu.controller;

import kr.co.hanhosung.wallbu.dto.LoginDto;
import kr.co.hanhosung.wallbu.dto.TokenDto;
import kr.co.hanhosung.wallbu.dto.UserDto;
import kr.co.hanhosung.wallbu.global.error.exception.AuthorizationException;
import kr.co.hanhosung.wallbu.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {
    public static final String REFRESH_TOKEN = "Refresh-Token";
    public static final String TOKEN_PREFIX = "Bearer ";

    private final AuthService authService;

    @PostMapping("/auth/signup")
    public ResponseEntity<Boolean> signUp(@Valid @RequestBody UserDto userDto) {
        assert (userDto != null);
        assert (userDto.getName() != null && userDto.getEmail() != null && userDto.getPassword() != null && userDto.getPhoneNumber() != null);

        authService.signUp(userDto);
        return new ResponseEntity<>(Boolean.TRUE, HttpStatus.CREATED);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginDto loginDto) {

        assert (loginDto != null);
        assert (loginDto.getId() != null && !loginDto.getId().isEmpty());
        assert (loginDto.getPassword() != null && !loginDto.getPassword().isEmpty());

        TokenDto tokenDto = authService.login(loginDto);

        assert (tokenDto != null);
        assert (tokenDto.getAccessToken() != null && tokenDto.getRefreshToken() != null);

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + tokenDto.getAccessToken())
                .header(REFRESH_TOKEN, TOKEN_PREFIX + tokenDto.getRefreshToken())
                .build();
    }


    @GetMapping("/auth/refresh")
    public ResponseEntity<Void> getAccessTokenByRefreshToken(HttpServletRequest request) {

        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        String refreshToken = request.getHeader(REFRESH_TOKEN);

        if (accessToken == null || refreshToken == null) {
            log.error("fail generate new access token");
            throw new AuthorizationException();
        }

        String generatedAccessToken = authService.getAccessTokenByRefreshToken(new TokenDto(accessToken, refreshToken));

        assert (generatedAccessToken != null && !generatedAccessToken.isEmpty());

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + generatedAccessToken)
                .build();
    }

}
