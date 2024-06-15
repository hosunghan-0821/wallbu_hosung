package kr.co.hanhosung.wallbu.service;


import kr.co.hanhosung.wallbu.domain.RefreshToken;
import kr.co.hanhosung.wallbu.domain.User;
import kr.co.hanhosung.wallbu.dto.LoginDto;
import kr.co.hanhosung.wallbu.dto.TokenDto;
import kr.co.hanhosung.wallbu.dto.UserDto;
import kr.co.hanhosung.wallbu.global.error.dto.ErrorCode;
import kr.co.hanhosung.wallbu.global.error.exception.AuthorizationException;
import kr.co.hanhosung.wallbu.global.error.exception.BusinessLogicException;
import kr.co.hanhosung.wallbu.global.util.encrypt.IHashService;
import kr.co.hanhosung.wallbu.global.util.token.ITokenManager;
import kr.co.hanhosung.wallbu.repository.ITokenRepository;
import kr.co.hanhosung.wallbu.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final IUserRepository iUserRepository;

    private final IHashService iHashService;

    private final ITokenManager iTokenManager;

    private final ITokenRepository iTokenRepository;


    @Transactional
    public void signUp(UserDto userDto) {

        assert (userDto != null);

        //phoneNumber 중복검사
        User findUserOrNull = iUserRepository.findUserByPhoneNumber(userDto.getPhoneNumber()).orElse(null);

        if (findUserOrNull != null) {
            throw new BusinessLogicException(ErrorCode.BUSINESS_LOGIC_EXCEPTION_USER_DUPLICATE);
        }

        //비밀번호 암호화
        //(client 로부터 암호화 거쳐서 오는게 맞은거 같으나.. 우선 백엔드에서 암호화 DB 저장 진행)
        String encodedPassword = iHashService.encode(userDto.getPassword());
        userDto.updatePasswordByEncrypt(encodedPassword);

        //유저 저장
        iUserRepository.save(userDto.toUserEntity());

    }

    @Transactional
    public TokenDto login(LoginDto loginDto) {

        assert (loginDto != null);

        String encodedPassword = iHashService.encode(loginDto.getPassword());
        User findUserOrNull = iUserRepository.findUserByPhoneNumberAndPassword(loginDto.getId(), encodedPassword).orElse(null);

        if (findUserOrNull == null) {
            throw new BusinessLogicException(ErrorCode.BUSINESS_LOGIC_EXCEPTION_USER_INFO_INVALID);
        }

        String accessToken = iTokenManager.createAccessToken(findUserOrNull.getId());
        String refreshToken = iTokenManager.createRefreshToken();


        iTokenRepository.save(new RefreshToken(findUserOrNull.getId(), refreshToken));


        return new TokenDto(accessToken, refreshToken);

    }

    @Transactional(readOnly = true)
    public String getAccessTokenByRefreshToken(TokenDto tokenDto) {

        assert (tokenDto != null);
        assert (tokenDto.getAccessToken() != null && tokenDto.getRefreshToken() != null);

        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();

        //1. Refresh Token 유효성 검증
        iTokenManager.verifyToken(refreshToken);

        //2. DB RefreshToken 검증 && accessToken id
        long userId = iTokenManager.decodeUserId(accessToken);
        iTokenRepository.findRefreshTokenByRefreshTokenAndUserId(refreshToken, userId).orElseThrow(AuthorizationException::new);

        //3. 새로운 accessToken 발급
        return iTokenManager.createAccessToken(userId);

    }
}
