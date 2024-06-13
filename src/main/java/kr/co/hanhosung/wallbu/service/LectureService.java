package kr.co.hanhosung.wallbu.service;

import kr.co.hanhosung.wallbu.domain.Lecture;
import kr.co.hanhosung.wallbu.domain.LectureUser;
import kr.co.hanhosung.wallbu.domain.User;
import kr.co.hanhosung.wallbu.domain.UserRole;
import kr.co.hanhosung.wallbu.dto.LectureDto;
import kr.co.hanhosung.wallbu.global.error.dto.ErrorCode;
import kr.co.hanhosung.wallbu.global.error.exception.AuthorizationException;
import kr.co.hanhosung.wallbu.global.error.exception.BusinessLogicException;
import kr.co.hanhosung.wallbu.repository.ILectureRepository;
import kr.co.hanhosung.wallbu.repository.ILectureUserRepository;
import kr.co.hanhosung.wallbu.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LectureService {

    private final ILectureRepository iLectureRepository;

    private final ILectureUserRepository iLectureUserRepository;

    private final IUserRepository iUserRepository;

    @Transactional
    public void registerLecture(LectureDto lectureDto, long userId) {

        assert (lectureDto != null);
        assert (userId > 0);

        User user = iUserRepository.findById(userId).orElseThrow(AuthorizationException::new);

        if (!user.getUserRole().equals(UserRole.PROFESSOR)) {
            throw new BusinessLogicException(ErrorCode.BUSINESS_LOGIC_EXCEPTION_USER_ROLE_INVALID);
        }

        iLectureRepository.save(lectureDto.toLectureEntity(user));

    }

    @Transactional
    public void enrollmentLecture(List<LectureDto> lectureDtoList, long userId) {

        assert (lectureDtoList != null && !lectureDtoList.isEmpty());
        assert (userId > 0);

        //유저정보확인
        User user = iUserRepository.findById(userId).orElseThrow(AuthorizationException::new);

        //해당 강좌 Lock 적용
        List<Long> lectureIdList = lectureDtoList.stream().map(LectureDto::getId).distinct().collect(Collectors.toList());
        List<Lecture> lectureList = iLectureRepository.findAllByIdWithLock(lectureIdList);

        //유효한 강좌id 가 존재 x.
        if (lectureList.isEmpty()) {
            throw new BusinessLogicException(ErrorCode.BUSINESS_LOGIC_EXCEPTION_INVALID_LECTURE);
        }

        //해당 강좌에 등록한 유저 가져오기 (중복 막기위해서)
        List<LectureUser> lectureUserList = iLectureUserRepository.findAllStudentByLectureIdList(lectureIdList);
        Map<Long, List<Long>> lectureUserMap = getLectureUserMap(lectureUserList);

        for (Lecture lecture : lectureList) {

            //정원 초과
            if (lecture.getStudentCount().longValue() >= lecture.getMaxStudentCount().longValue()) {
                throw new BusinessLogicException(ErrorCode.BUSINESS_LOGIC_EXCEPTION_NOT_REMAIN_SEAT_IN_LECTURE);
            }
            //이미 등록되 있으면 오류 or 패스 (?) .. -> 일단 오류
            if (lectureUserMap.get(lecture.getId()) != null && lectureUserMap.get(lecture.getId()).contains(userId)) {
                throw new BusinessLogicException(ErrorCode.BUSINESS_LOGIC_EXCEPTION_ALREADY_ENROLLMENT);
            }

            LectureUser lectureUser = new LectureUser(user, lecture);
            iLectureUserRepository.save(lectureUser);

            lecture.upStudentCount();
        }

    }

    private Map<Long, List<Long>> getLectureUserMap(List<LectureUser> lectureUserList) {
        Map<Long, List<Long>> lectureUserMap = new HashMap<>();
        //상품 개수 정보 추출
        for (LectureUser lectureUser : lectureUserList) {
            Long lectureId = lectureUser.getLecture().getId();
            Long userId = lectureUser.getUser().getId();

            if (!lectureUserMap.containsKey(lectureId)) {
                lectureUserMap.put(lectureId, new ArrayList<>(Arrays.asList(userId)));
            } else {
                List<Long> userIdList = lectureUserMap.get(lectureId);
                userIdList.add(userId);
            }
        }
        return lectureUserMap;
    }
}
