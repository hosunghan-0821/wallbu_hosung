package kr.co.hanhosung.wallbu.service;

import kr.co.hanhosung.wallbu.domain.Lecture;
import kr.co.hanhosung.wallbu.domain.LectureUser;
import kr.co.hanhosung.wallbu.domain.User;
import kr.co.hanhosung.wallbu.domain.UserRole;
import kr.co.hanhosung.wallbu.dto.LectureDto;
import kr.co.hanhosung.wallbu.dto.UserDto;
import kr.co.hanhosung.wallbu.global.error.exception.BusinessLogicException;
import kr.co.hanhosung.wallbu.repository.ILectureRepository;
import kr.co.hanhosung.wallbu.repository.ILectureUserRepository;
import kr.co.hanhosung.wallbu.repository.IUserRepository;
import kr.co.hanhosung.wallbu.repository.LectureCustomRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;


@ExtendWith(MockitoExtension.class)
class LectureServiceTest {

    @InjectMocks
    private LectureService lectureService;

    @Mock
    private ILectureRepository iLectureRepository;

    @Mock
    private ILectureUserRepository iLectureUserRepository;

    @Mock
    private IUserRepository iUserRepository;
    @Mock
    private LectureCustomRepository lectureCustomRepository;

    @Test
    @DisplayName("[성공] : 강좌 등록 ")
    void registerLecture() {

        //given
        User user = new User("hosung", "winsomed96@naver.com", "01012345678", "ghtjd114", UserRole.PROFESSOR);
        Optional<User> optionalUser = Optional.of(user);
        Mockito.doReturn(optionalUser).when(iUserRepository).findById(any(Long.class));
        Mockito.doReturn(new Lecture()).when(iLectureRepository).save(any(Lecture.class));

        LectureDto lectureDto = LectureDto.builder().title("강좌").maxStudentCount(10L).price(10_000L).build();
        //when then
        lectureService.registerLecture(lectureDto, 1L);

    }

    @Test
    @DisplayName("[실패] : 강좌 등록  - 유저 (수강생 회원) 일 경우")
    void registerLectureFail() {

        //given
        User user = new User("hosung", "winsomed96@naver.com", "01012345678", "ghtjd114", UserRole.STUDENT);
        Optional<User> optionalUser = Optional.of(user);
        Mockito.doReturn(optionalUser).when(iUserRepository).findById(any(Long.class));

        LectureDto lectureDto = LectureDto.builder().title("강좌").maxStudentCount(10L).price(10_000L).build();
        //when then
        Assertions.assertThrows(BusinessLogicException.class, () -> lectureService.registerLecture(lectureDto, 1L));

    }

    @Test
    @DisplayName("[성공] : 강좌 신청")
    void enrollmentLecture() {

        //given
        User user = new User("hosung", "winsomed96@naver.com", "01012345678", "ghtjd114", UserRole.PROFESSOR);
        Optional<User> optionalUser = Optional.of(user);

        User user2 = new User("posung", "winsomed96@naver.com", "01012345610", "ghtjd114", UserRole.STUDENT);


        Lecture lecture = new Lecture("title", 10L, 10_000L, user);
        List<Lecture> lectureList = new ArrayList<>(Arrays.asList(lecture));

        List<LectureUser> lectureUserList = new ArrayList<>(Arrays.asList(new LectureUser(user2, lecture)));

        Mockito.doReturn(optionalUser).when(iUserRepository).findById(any(Long.class));
        Mockito.doReturn(lectureList).when(iLectureRepository).findAllByIdWithLock(any(List.class));
        Mockito.doReturn(lectureUserList).when(iLectureUserRepository).findAllStudentByLectureIdList(any(List.class));
        Mockito.doReturn(new LectureUser()).when(iLectureUserRepository).save(any(LectureUser.class));
        //when

        lectureService.enrollmentLecture(new ArrayList<>(Arrays.asList(LectureDto.builder().id(1L).build())), 1L);
        //then
        Assertions.assertEquals(1, lecture.getStudentCount());
    }


    @Test
    @DisplayName("[실패] : 강좌 신청 - 유효한 강좌 id 없을 경우")
    void enrollmentLectureFail() {

        //given
        User user = new User("hosung", "winsomed96@naver.com", "01012345678", "ghtjd114", UserRole.PROFESSOR);
        Optional<User> optionalUser = Optional.of(user);

        User user2 = new User("posung", "winsomed96@naver.com", "01012345610", "ghtjd114", UserRole.STUDENT);


        Lecture lecture = new Lecture("title", 10L, 10_000L, user);
        List<Lecture> lectureList = new ArrayList<>();

        List<LectureUser> lectureUserList = new ArrayList<>(Arrays.asList(new LectureUser(user2, lecture)));

        Mockito.doReturn(optionalUser).when(iUserRepository).findById(any(Long.class));
        Mockito.doReturn(lectureList).when(iLectureRepository).findAllByIdWithLock(any(List.class));

        //when then
        Assertions.assertThrows(BusinessLogicException.class, () -> lectureService.enrollmentLecture(new ArrayList<>(Arrays.asList(LectureDto.builder().id(1L).build())), 1L));
    }

    @Test
    @DisplayName("[실패] : 강좌 신청 - 정원 초과")
    void enrollmentLectureFail2() {

        //given
        User user = new User("hosung", "winsomed96@naver.com", "01012345678", "ghtjd114", UserRole.PROFESSOR);
        Optional<User> optionalUser = Optional.of(user);

        User user2 = new User("posung", "winsomed96@naver.com", "01012345610", "ghtjd114", UserRole.STUDENT);


        Lecture lecture = new Lecture("title", 10L, 10_000L, user);
        for (int i = 0; i < 10; i++) {
            lecture.upStudentCount();
        }

        List<Lecture> lectureList = new ArrayList<>(Arrays.asList(lecture));

        List<LectureUser> lectureUserList = new ArrayList<>(Arrays.asList(new LectureUser(user2, lecture)));

        Mockito.doReturn(optionalUser).when(iUserRepository).findById(any(Long.class));
        Mockito.doReturn(lectureList).when(iLectureRepository).findAllByIdWithLock(any(List.class));
        Mockito.doReturn(lectureUserList).when(iLectureUserRepository).findAllStudentByLectureIdList(any(List.class));

        //when then
        Assertions.assertThrows(BusinessLogicException.class, () -> lectureService.enrollmentLecture(new ArrayList<>(Arrays.asList(LectureDto.builder().id(1L).build())), 1L));

    }

    @Test
    @DisplayName("[실패] : 강좌 신청 - 이미 신청된 경우")
    void enrollmentLectureFail3() {

        //given
        User user = new User(1L, "hosung", "winsomed96@naver.com", "01012345678", "ghtjd114", UserRole.PROFESSOR);
        Optional<User> optionalUser = Optional.of(user);

        User user2 = new User("posung", "winsomed96@naver.com", "01012345610", "ghtjd114", UserRole.STUDENT);


        Lecture lecture = new Lecture("title", 10L, 10_000L, user);

        lecture.upStudentCount();


        List<Lecture> lectureList = new ArrayList<>(Arrays.asList(lecture));

        List<LectureUser> lectureUserList = new ArrayList<>(Arrays.asList(new LectureUser(user, lecture)));

        Mockito.doReturn(optionalUser).when(iUserRepository).findById(any(Long.class));
        Mockito.doReturn(lectureList).when(iLectureRepository).findAllByIdWithLock(any(List.class));
        Mockito.doReturn(lectureUserList).when(iLectureUserRepository).findAllStudentByLectureIdList(any(List.class));

        //when then
        Assertions.assertThrows(BusinessLogicException.class, () -> lectureService.enrollmentLecture(new ArrayList<>(Arrays.asList(LectureDto.builder().id(1L).build())), 1L));

    }
}