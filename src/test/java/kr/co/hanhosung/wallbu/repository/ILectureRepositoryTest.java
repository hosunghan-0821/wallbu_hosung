package kr.co.hanhosung.wallbu.repository;

import kr.co.hanhosung.wallbu.config.DbTestConfiguration;
import kr.co.hanhosung.wallbu.domain.Lecture;
import kr.co.hanhosung.wallbu.domain.User;
import kr.co.hanhosung.wallbu.domain.UserRole;
import kr.co.hanhosung.wallbu.service.enumerate.SortingType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


@Import(DbTestConfiguration.class)
@DataJpaTest
@ActiveProfiles("test")
class ILectureRepositoryTest {

    @Autowired
    private IUserRepository iUserRepository;
    @Autowired
    private ILectureRepository iLectureRepository;
    @Autowired
    private LectureCustomRepository lectureCustomRepository;

    @Test
    @DisplayName("[성공] : 강좌 정보 조회하기")
    void findAllByIdWithLock() {

        //given
        User user = new User("hosung", "winsomed96@naver.com", "01012345678", "ghtjd114", UserRole.STUDENT);
        iUserRepository.save(user);

        Lecture lecture1 = new Lecture("강좌1", 10L, 10_000L, user);
        iLectureRepository.save(lecture1);

        Lecture lecture2 = new Lecture("강좌2", 9L, 10_000L, user);
        iLectureRepository.save(lecture2);

        Lecture lecture3 = new Lecture("강좌3", 8L, 10_000L, user);
        iLectureRepository.save(lecture3);

        //when\
        List<Lecture> lectureList = iLectureRepository.findAllByIdWithLock(new ArrayList<>(Arrays.asList(lecture1.getId(), lecture2.getId(), lecture3.getId())));

        //then
        List<Long> collect = lectureList.stream().map(Lecture::getId).collect(Collectors.toList());
        Assertions.assertEquals(3, lectureList.size());
        Assertions.assertTrue(collect.contains(lecture1.getId()));
        Assertions.assertTrue(collect.contains(lecture2.getId()));
        Assertions.assertTrue(collect.contains(lecture3.getId()));

    }

    @Test
    @DisplayName("[성공] : 강좌 정보 페이징 - 최신순 정렬")
    void pagingLectureWithSortingTypeRecent() {
        //given
        iLectureRepository.deleteAll();
        User user = new User("hosung", "winsomed96@naver.com", "01012345678", "ghtjd114", UserRole.STUDENT);
        iUserRepository.save(user);

        List<Lecture> lectureList = new ArrayList<>();
        for (int i = 0; i < 21; i++) {
            Lecture lecture = new Lecture("강좌" + i, 10L, 10_000L, user);
            lectureList.add(lecture);
            iLectureRepository.save(lecture);
        }

        lectureList.sort(Comparator.comparingLong(Lecture::getId).reversed());
        //when
        Page<Lecture> pagingLectureWithSortingType = lectureCustomRepository.pagingLectureWithSortingType(PageRequest.of(0, 20), SortingType.RECENTLY_REGISTERED);

        //then
        List<Lecture> pagedLectureList = pagingLectureWithSortingType.getContent();

        Assertions.assertEquals(20, pagedLectureList.size());
        for (int i = 0; i < pagedLectureList.size(); i++) {
            Assertions.assertEquals(lectureList.get(i).getId(), pagedLectureList.get(i).getId());
        }
    }


    @Test
    @DisplayName("[성공] : 강좌 정보 페이징 - 참여 숫자 많은 순")
    void pagingLectureWithSortingTypeHighUser() {
        //given
        iLectureRepository.deleteAll();

        User user = new User("hosung", "winsomed96@naver.com", "01012345678", "ghtjd114", UserRole.STUDENT);
        iUserRepository.save(user);

        List<Lecture> lectureList = new ArrayList<>();
        for (int i = 0; i < 21; i++) {
            Lecture lecture = new Lecture("강좌" + i, 30L, 10_000L, user);
            for (int j = 20 - i; j > 0; j--) {
                lecture.upStudentCount();
            }
            lectureList.add(lecture);
            iLectureRepository.save(lecture);
        }

        lectureList.sort(Comparator.comparingLong(Lecture::getId));
        //when
        Page<Lecture> pagingLectureWithSortingType = lectureCustomRepository.pagingLectureWithSortingType(PageRequest.of(0, 20), SortingType.HIGHEST_STUDENTS_RATE);

        //then
        List<Lecture> pagedLectureList = pagingLectureWithSortingType.getContent();

        Assertions.assertEquals(20, pagedLectureList.size());
        for (int i = 0; i < pagedLectureList.size(); i++) {
            Assertions.assertEquals(lectureList.get(i).getId(), pagedLectureList.get(i).getId());
        }
    }


    @Test
    @DisplayName("[성공] : 강좌 정보 페이징 - 참여율 많은 순")
    void pagingLectureWithSortingTypeHighRate() {
        //given
        iLectureRepository.deleteAll();

        User user = new User("hosung", "winsomed96@naver.com", "01012345678", "ghtjd114", UserRole.STUDENT);
        iUserRepository.save(user);


        Lecture lecture1 = new Lecture("강좌1", 20L, 10_000L, user);
        loopUpStudentCountAndSave(lecture1, 2); // 2/20 == 0.1

        Lecture lecture2 = new Lecture("강좌1", 20L, 10_000L, user);
        loopUpStudentCountAndSave(lecture2, 3); // 3/20  == 0.15

        Lecture lecture3 = new Lecture("강좌1", 20L, 10_000L, user);
        loopUpStudentCountAndSave(lecture3, 5); // 5/20  == 0.25

        Lecture lecture4 = new Lecture("강좌1", 10L, 10_000L, user);
        loopUpStudentCountAndSave(lecture4, 2); // 2/10  == 0.2

        Lecture lecture5 = new Lecture("강좌1", 10L, 10_000L, user);
        loopUpStudentCountAndSave(lecture5, 3); // 3/10  == 0.3

        List<Lecture> lectureList = new ArrayList<>(Arrays.asList(lecture1,lecture2,lecture3,lecture4,lecture5));

        lectureList.sort((v1, v2) -> Double.compare(
                v2.getStudentCount() / (double) v2.getMaxStudentCount(),
                v1.getStudentCount() / (double) v1.getMaxStudentCount()));

        //when
        Page<Lecture> pagingLectureWithSortingType = lectureCustomRepository.pagingLectureWithSortingType(PageRequest.of(0, 20), SortingType.HIGHEST_STUDENTS_RATE);

        //then
        List<Lecture> pagedLectureList = pagingLectureWithSortingType.getContent();

        Assertions.assertEquals(5, pagedLectureList.size());
        Assertions.assertEquals(pagedLectureList.get(0).getId(), lecture5.getId());
        Assertions.assertEquals(pagedLectureList.get(1).getId(), lecture3.getId());
        Assertions.assertEquals(pagedLectureList.get(2).getId(), lecture4.getId());
        Assertions.assertEquals(pagedLectureList.get(3).getId(), lecture2.getId());
        Assertions.assertEquals(pagedLectureList.get(4).getId(), lecture1.getId());
    }

    private void loopUpStudentCountAndSave(Lecture lecture, int upStudentCount) {
        for (int i = 0; i < upStudentCount; i++) {
            lecture.upStudentCount();
        }
        iLectureRepository.save(lecture);
    }
}