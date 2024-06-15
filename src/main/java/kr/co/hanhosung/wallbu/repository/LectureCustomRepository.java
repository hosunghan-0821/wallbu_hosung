package kr.co.hanhosung.wallbu.repository;

import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.hanhosung.wallbu.domain.Lecture;
import kr.co.hanhosung.wallbu.domain.QUser;
import kr.co.hanhosung.wallbu.service.enumerate.SortingType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

import static kr.co.hanhosung.wallbu.domain.QLecture.lecture;
import static kr.co.hanhosung.wallbu.domain.QUser.user;


@RequiredArgsConstructor
@Repository
public class LectureCustomRepository {

    private final JPAQueryFactory queryFactory;

    public Page<Lecture> pagingLectureWithSortingType(Pageable pageable, SortingType sortingType) {

        JPAQuery<Lecture> lectureJPAQuery = null;
        switch (sortingType) {
            case RECENTLY_REGISTERED:
                lectureJPAQuery = queryFactory
                        .selectFrom(lecture)
                        .leftJoin(lecture.createUser, user).fetchJoin()
                        .orderBy(lecture.id.desc());

                break;
            case MOST_STUDENTS:
                lectureJPAQuery = queryFactory
                        .selectFrom(lecture)
                        .leftJoin(lecture.createUser, user).fetchJoin()
                        .orderBy(lecture.studentCount.desc());
                break;
            case HIGHEST_STUDENTS_RATE:
                NumberExpression<Double> sortExpression = lecture.studentCount.doubleValue().divide(lecture.maxStudentCount);

                lectureJPAQuery = queryFactory
                        .selectFrom(lecture)
                        .leftJoin(lecture.createUser, user).fetchJoin()
                        .orderBy(sortExpression.desc());
                break;
            default:
                assert (false) : "invalid enum value sortingType ";
        }
        assert (lectureJPAQuery != null);

        List<Lecture> resultLectureList = lectureJPAQuery.offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();
        Long totalCount = queryFactory.select(lecture.count()).from(lecture).fetchOne();
        assert (totalCount != null);

        return new PageImpl<>(resultLectureList, pageable, totalCount);
    }
}
