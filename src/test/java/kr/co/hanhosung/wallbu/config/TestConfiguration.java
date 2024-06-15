package kr.co.hanhosung.wallbu.config;

import kr.co.hanhosung.wallbu.controller.AuthController;
import kr.co.hanhosung.wallbu.global.util.token.ITokenManager;
import kr.co.hanhosung.wallbu.service.AuthService;
import kr.co.hanhosung.wallbu.service.LectureService;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

@org.springframework.boot.test.context.TestConfiguration
public class TestConfiguration {

    @MockBean
    private AuthService authService;

    @MockBean
    private LectureService lectureService;

    @MockBean
    private ITokenManager iTokenManager;
}
