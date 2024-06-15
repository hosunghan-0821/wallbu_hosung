package kr.co.hanhosung.wallbu.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kr.co.hanhosung.wallbu.domain.UserRole;
import kr.co.hanhosung.wallbu.dto.LoginDto;
import kr.co.hanhosung.wallbu.dto.TokenDto;
import kr.co.hanhosung.wallbu.dto.UserDto;
import kr.co.hanhosung.wallbu.global.annotation.LoginUserArgumentResolver;
import kr.co.hanhosung.wallbu.global.util.token.ITokenManager;
import kr.co.hanhosung.wallbu.global.util.token.JwtDecoder;
import kr.co.hanhosung.wallbu.global.util.token.JwtTokenProvider;
import kr.co.hanhosung.wallbu.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static kr.co.hanhosung.wallbu.controller.AuthController.REFRESH_TOKEN;
import static kr.co.hanhosung.wallbu.controller.AuthController.TOKEN_PREFIX;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @InjectMocks
    AuthController authController;

    @Mock
    private AuthService authService;

    private ObjectMapper objectMapper;
    private MockMvc mockMvc;

    private ITokenManager iTokenManager = new JwtTokenProvider("wallbuhosung", new JwtDecoder("wallbuhosung"));

    private UserDto.UserDtoBuilder userDtoBuilder() {

        return UserDto.builder()
                .userRole(UserRole.STUDENT)
                .name("한호성")
                .phoneNumber("01012345678")
                .password("ghtjd114")
                .email("winsomed96@naver.com");
    }


    @BeforeEach
    public void init() {

        mockMvc = MockMvcBuilders
                .standaloneSetup(authController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver(), new LoginUserArgumentResolver(iTokenManager)).build();
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("[성공] : 회원가입 API Controller 테스트")
    void signUp() throws Exception {
        //given
        UserDto userDto = userDtoBuilder().build();
        Mockito.doNothing().when(authService).signUp(any(UserDto.class));
        String request = objectMapper.writeValueAsString(userDto);

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)

        );

        //then
        resultActions
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("[성공] : 로그인 API Controller 테스트")
    void login() throws Exception {

        //given
        LoginDto loginDto = new LoginDto("01095751234", "ghtjd114");
        String accessToken = iTokenManager.createAccessToken(1L);
        String refreshToken = iTokenManager.createRefreshToken();
        TokenDto tokenDto = new TokenDto(accessToken, refreshToken);

        Mockito.doReturn(tokenDto).when(authService).login(any(LoginDto.class));

        //when
        String request = objectMapper.writeValueAsString(loginDto);
        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
        );
        //then
        resultActions
                .andExpect(header().string(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + accessToken))
                .andExpect(header().string(REFRESH_TOKEN, TOKEN_PREFIX + refreshToken));
    }

    @Test
    @DisplayName("[성공] : 에세스 토큰 재발행 API Controller 테스트")
    void getAccessTokenByRefreshToken() throws Exception {

        //given

        String expiredAccessToken = iTokenManager.createAccessToken(1L);
        String refreshToken = iTokenManager.createRefreshToken();

        String newAccessToken = iTokenManager.createAccessToken(1L);

        Mockito.doReturn(newAccessToken).when(authService).getAccessTokenByRefreshToken(any());

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION,expiredAccessToken)
                        .header(REFRESH_TOKEN,refreshToken)
        );

        //then
        resultActions
                .andExpect(header().string(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + newAccessToken));

    }
}