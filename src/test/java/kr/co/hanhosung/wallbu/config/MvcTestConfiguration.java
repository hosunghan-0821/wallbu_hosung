package kr.co.hanhosung.wallbu.config;

import kr.co.hanhosung.wallbu.global.util.token.ITokenManager;
import kr.co.hanhosung.wallbu.service.AuthService;
import kr.co.hanhosung.wallbu.service.LectureService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;

@TestConfiguration
public class MvcTestConfiguration {

    @MockBean
    private AuthService authService;

    @MockBean
    private LectureService lectureService;

    @MockBean
    private ITokenManager iTokenManager;
}
