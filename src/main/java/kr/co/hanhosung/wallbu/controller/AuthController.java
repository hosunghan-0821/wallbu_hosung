package kr.co.hanhosung.wallbu.controller;

import kr.co.hanhosung.wallbu.dto.SignUpInfoDto;
import kr.co.hanhosung.wallbu.dto.UserDto;
import kr.co.hanhosung.wallbu.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public void login() {

    }


    @GetMapping("/auth/refresh")
    public void getAccessTokenByRefreshToken(HttpServletRequest request) {
        log.info("get Refresh");
    }

}
