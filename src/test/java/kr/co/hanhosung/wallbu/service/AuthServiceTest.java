package kr.co.hanhosung.wallbu.service;

import kr.co.hanhosung.wallbu.domain.RefreshToken;
import kr.co.hanhosung.wallbu.domain.User;
import kr.co.hanhosung.wallbu.domain.UserRole;
import kr.co.hanhosung.wallbu.dto.LoginDto;
import kr.co.hanhosung.wallbu.dto.TokenDto;
import kr.co.hanhosung.wallbu.dto.UserDto;
import kr.co.hanhosung.wallbu.global.error.exception.BusinessLogicException;
import kr.co.hanhosung.wallbu.global.util.encrypt.IHashService;
import kr.co.hanhosung.wallbu.global.util.token.ITokenManager;
import kr.co.hanhosung.wallbu.repository.ITokenRepository;
import kr.co.hanhosung.wallbu.repository.IUserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;
    @Mock
    private IUserRepository iUserRepository;

    @Mock
    private IHashService iHashService;

    @Mock
    private ITokenManager iTokenManager;
    @Mock
    private ITokenRepository iTokenRepository;

    @Test
    @DisplayName("[성공] : 회원가입")
    void signUp() {
        //given
        User user = new User("hosung", "winsomed96@naver.com", "01012345678", "ghtjd114", UserRole.PROFESSOR);
        UserDto userDto = UserDto.builder().name("hosung").password("ghtjd114").email("winsomed96@naver.com").phoneNumber("01055557777").userRole(UserRole.PROFESSOR).build();
        Optional<User> optionalUser = Optional.of(user);

        Mockito.doReturn(Optional.ofNullable(null)).when(iUserRepository).findUserByPhoneNumber(any(String.class));
        String encodedPassword = "encoded password";
        Mockito.doReturn(encodedPassword).when(iHashService).encode(any(String.class));
        //when

        authService.signUp(userDto);
        //then
        Assertions.assertEquals(encodedPassword, userDto.getPassword());
    }

    @Test
    @DisplayName("[실패] : 회원가입 - 중복 아이디")
    void signUpFail() {
        //given
        User user = new User("hosung", "winsomed96@naver.com", "01012345678", "ghtjd114", UserRole.PROFESSOR);
        UserDto userDto = UserDto.builder().name("hosung").password("ghtjd114").email("winsomed96@naver.com").phoneNumber("01055557777").userRole(UserRole.PROFESSOR).build();
        Optional<User> optionalUser = Optional.of(user);

        Mockito.doReturn(optionalUser).when(iUserRepository).findUserByPhoneNumber(any(String.class));

        //when, then
        Assertions.assertThrows(BusinessLogicException.class, () -> authService.signUp(userDto));

    }

    @Test
    @DisplayName("[성공] : 로그인")
    void login() {
        //given
        User user = new User(1L,"hosung", "winsomed96@naver.com", "01012345678", "ghtjd114", UserRole.PROFESSOR);
        Optional<User> optionalUser = Optional.of(user);

        LoginDto loginDto = new LoginDto("01095756302", "ghtjd114");
        Mockito.doReturn(optionalUser).when(iUserRepository).findUserByPhoneNumberAndPassword(any(String.class), any(String.class));
        String encodedPassword = "encoded password";
        Mockito.doReturn(encodedPassword).when(iHashService).encode(any(String.class));

        String accessToken = "accessToken";
        Mockito.doReturn(accessToken).when(iTokenManager).createAccessToken(1L);

        String refreshToken = "refreshToken";
        Mockito.doReturn(refreshToken).when(iTokenManager).createRefreshToken();

        Mockito.doReturn(new RefreshToken(1L,refreshToken)).when(iTokenRepository).save(any(RefreshToken.class));
        //when

        TokenDto tokenDto = authService.login(loginDto);
        //then
        Assertions.assertEquals(accessToken, tokenDto.getAccessToken());
        Assertions.assertEquals(refreshToken, tokenDto.getRefreshToken());
    }

}