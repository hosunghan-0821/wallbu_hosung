package kr.co.hanhosung.wallbu.concurrency;

import kr.co.hanhosung.wallbu.WallbuApplication;
import kr.co.hanhosung.wallbu.domain.Lecture;
import kr.co.hanhosung.wallbu.domain.User;
import kr.co.hanhosung.wallbu.domain.UserRole;
import kr.co.hanhosung.wallbu.dto.LectureDto;
import kr.co.hanhosung.wallbu.repository.ILectureRepository;
import kr.co.hanhosung.wallbu.repository.ILectureUserRepository;
import kr.co.hanhosung.wallbu.repository.IUserRepository;
import kr.co.hanhosung.wallbu.service.LectureService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

@SpringBootTest(classes = WallbuApplication.class)
@ActiveProfiles("test")
public class ConcurrencyTest {

    @Autowired
    private LectureService lectureService;

    @Autowired
    private ILectureRepository iLectureRepository;

    @Autowired
    private ILectureUserRepository iLectureUserRepository;
    @Autowired
    private IUserRepository iUserRepository;

    private AtomicLong count = new AtomicLong(1L);

    @Test
    @DisplayName("[성공] : 강좌 등록 동시성 Test")
    public void lectureRegisterConcurrencyTest() throws InterruptedException {

        List<User> userList = new ArrayList<>();
        //given 유저,강좌 세팅
        int threadCnt = 100;
        for (int i = 0; i < 101; i++) {
            User user = new User("name " + i, i + "123@naver.com", "0101234567" + i, "test", UserRole.STUDENT);
            iUserRepository.save(user);
            userList.add(user);
        }

        Lecture lecture = new Lecture("제목", 2000L, 10000L, userList.get(0));
        iLectureRepository.save(lecture);
        LectureDto lectureDto = LectureDto.builder().id(lecture.getId()).build();
        //when
        ExecutorService executorService = Executors.newFixedThreadPool(threadCnt);
        CountDownLatch countDownLatch = new CountDownLatch(threadCnt);


        for (int i = 0; i < threadCnt; i++) {

            executorService.execute(() -> {
                try {
                    lectureService.enrollmentLecture(new ArrayList<>(Arrays.asList(lectureDto)), count.incrementAndGet());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            });

        }
        countDownLatch.await();


        //then
        Lecture resultLecture = iLectureRepository.findById(lecture.getId()).orElse(null);
        assert resultLecture != null;
        Assertions.assertThat(resultLecture.getStudentCount()).isEqualTo(threadCnt);

    }
}
