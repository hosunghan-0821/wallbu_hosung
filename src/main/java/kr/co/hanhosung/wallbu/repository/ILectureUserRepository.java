package kr.co.hanhosung.wallbu.repository;

import kr.co.hanhosung.wallbu.domain.Lecture;
import kr.co.hanhosung.wallbu.domain.LectureUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ILectureUserRepository extends JpaRepository<LectureUser, Long> {

    @Query(value = "SELECT lu from LectureUser lu " +
            "join fetch lu.lecture as l " +
            "join fetch lu.user as u " +
            "WHERE l.id IN :ids ")
    List<LectureUser> findAllStudentByLectureIdList(@Param(value = "ids") List<Long> lectureIdList);
}
