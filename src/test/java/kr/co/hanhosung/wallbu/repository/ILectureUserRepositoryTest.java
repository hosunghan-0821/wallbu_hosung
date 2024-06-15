package kr.co.hanhosung.wallbu.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.hanhosung.wallbu.config.DbTestConfiguration;
import kr.co.hanhosung.wallbu.config.MvcTestConfiguration;
import kr.co.hanhosung.wallbu.domain.Lecture;
import kr.co.hanhosung.wallbu.domain.LectureUser;
import kr.co.hanhosung.wallbu.domain.User;
import kr.co.hanhosung.wallbu.domain.UserRole;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Import(DbTestConfiguration.class)
@DataJpaTest
@ActiveProfiles("test")
class ILectureUserRepositoryTest {


    @Autowired
    private ILectureUserRepository iLectureUserRepository;

    @Autowired
    private ILectureRepository iLectureRepository;

    @Autowired
    private IUserRepository iUserRepository;


    @Test
    @DisplayName("[성공] : 강좌 등록한 유저정보 불러오기")
    void findAllStudentByLectureIdList() {

        //given
        User user = new User("hosung", "winsomed96@naver.com", "01012345678", "ghtjd114", UserRole.STUDENT);
        iUserRepository.save(user);
        Lecture lecture = new Lecture("강좌11", 10L, 10_000L, user);
        iLectureRepository.save(lecture);
        LectureUser lectureUser = new LectureUser(user, lecture);
        iLectureUserRepository.save(lectureUser);

        //when

        List<LectureUser> lectureUserList = iLectureUserRepository.findAllStudentByLectureIdList(new ArrayList<>(Arrays.asList(lecture.getId())));

        //then
        Assertions.assertEquals(1,lectureUserList.size());
        Assertions.assertNotNull(lectureUserList.get(0).getUser());
        Assertions.assertNotNull(lectureUserList.get(0).getLecture());
    }
}