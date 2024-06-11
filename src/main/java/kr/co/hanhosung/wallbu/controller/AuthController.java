package kr.co.hanhosung.wallbu.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    @PostMapping("/auth/login")
    public void login(){
        log.info("post /auth/login");
    }

    @PostMapping("/auth/signup")
    public void signUp(){
        log.info("post auth/signup");
    }

    @GetMapping("/auth/refresh")
    public void getAccessTokenByRefreshToken(HttpServletRequest request){
        log.info("get Refresh");
    }

}
