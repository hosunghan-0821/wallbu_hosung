package kr.co.hanhosung.wallbu.service;


import kr.co.hanhosung.wallbu.domain.User;
import kr.co.hanhosung.wallbu.dto.UserDto;
import kr.co.hanhosung.wallbu.global.error.dto.ErrorCode;
import kr.co.hanhosung.wallbu.global.error.exception.BusinessLogicException;
import kr.co.hanhosung.wallbu.global.util.encrypt.IHashService;
import kr.co.hanhosung.wallbu.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final IUserRepository IUserRepository;

    private final IHashService iHashService;

    public void signUp(UserDto userDto) {

        //phoneNumber 중복검사
        User findUserOrNull = IUserRepository.findUserByPhoneNumber(userDto.getPhoneNumber()).orElse(null);

        if (findUserOrNull != null) {
            throw new BusinessLogicException(ErrorCode.BUSINESS_LOGIC_EXCEPTION_USER_DUPLICATE);
        }

        //비밀번호 암호화
        //(client 로부터 암호화 거쳐서 오는게 맞은거 같으나.. 우선 백엔드에서 암호화 DB 저장 진행)
        String encodedPassword = iHashService.encode(userDto.getPassword());
        userDto.updatePasswordByEncrypt(encodedPassword);


        //유저 저장
        IUserRepository.save(userDto.toUserEntity());

    }
}
