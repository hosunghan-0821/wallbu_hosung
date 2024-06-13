package kr.co.hanhosung.wallbu.repository;

import kr.co.hanhosung.wallbu.domain.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.util.List;

public interface ILectureRepository extends JpaRepository<Lecture, Long> {

    @Query(value = "SELECT l from Lecture l " +
            "WHERE l.id IN :ids")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Lecture> findAllByIdWithLock(@Param(value = "ids") List<Long> lectureIdList);
}
