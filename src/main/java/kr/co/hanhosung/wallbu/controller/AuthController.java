package kr.co.hanhosung.wallbu.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "유저 인증 컨트롤러", description = "유저 인증 API들이 존재합니다. (로그인, 회원가입, 토큰 재발행)")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {
    public static final String REFRESH_TOKEN = "Refresh-Token";
    public static final String TOKEN_PREFIX = "Bearer ";

    private final AuthService authService;

    @Operation(summary = "회원가입API")
    @ApiResponse(responseCode = "201",description = "회원가입 성공")
    @ApiResponse(responseCode = "400",description = "회원가입 실패 유효성 검사 or 중복유저")
    @PostMapping("/auth/signup")
    public ResponseEntity<Boolean> signUp(@Valid @RequestBody UserDto userDto) {
        assert (userDto != null);
        assert (userDto.getName() != null && userDto.getEmail() != null && userDto.getPassword() != null && userDto.getPhoneNumber() != null);

        authService.signUp(userDto);
        return new ResponseEntity<>(Boolean.TRUE, HttpStatus.CREATED);
    }

    @Operation(summary = "로그인 API")
    @ApiResponse(responseCode = "200",description = "로그인 성공 - 성공시 헤더에 토큰값 저장해서 반환")
    @ApiResponse(responseCode = "400",description = "로그인 정보 유효성 검사 실패 or 유저정보 DB 존재 X")
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


    @Operation(summary = "새로운 access token 발급 API")
    @ApiResponse(responseCode = "200",description = "access token 발급 성공 - 성공시 헤더에 토큰값 저장해서 반환")
    @ApiResponse(responseCode = "401",description = "토큰 정보 유효하지 않음 or 누락")
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
