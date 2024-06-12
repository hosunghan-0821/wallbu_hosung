package kr.co.hanhosung.wallbu.controller;

import kr.co.hanhosung.wallbu.dto.LoginDto;
import kr.co.hanhosung.wallbu.dto.TokenDto;
import kr.co.hanhosung.wallbu.dto.UserDto;
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

        assert(tokenDto != null);
        assert(tokenDto.getAccessToken() != null && tokenDto.getRefreshToken() != null);

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDto.getAccessToken())
                .header("Refresh-Token", "Bearer " + tokenDto.getRefreshToken())
                .build();
    }


    @GetMapping("/auth/refresh")
    public void getAccessTokenByRefreshToken(HttpServletRequest request) {
        log.info("get Refresh");
    }

}
