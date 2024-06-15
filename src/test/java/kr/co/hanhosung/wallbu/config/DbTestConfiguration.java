package kr.co.hanhosung.wallbu.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.hanhosung.wallbu.repository.LectureCustomRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@TestConfiguration
public class DbTestConfiguration {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory(){
        return new JPAQueryFactory(entityManager);
    }

    @Bean
    public LectureCustomRepository lectureCustomRepository(){
        return new LectureCustomRepository(jpaQueryFactory());
    }
}
