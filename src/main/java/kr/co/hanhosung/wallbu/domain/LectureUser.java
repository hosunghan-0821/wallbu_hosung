package kr.co.hanhosung.wallbu.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Getter
@Setter(AccessLevel.PROTECTED)
@Table(name = "tb_lecture_user")
public class LectureUser extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Lecture lecture;

    public LectureUser() {
    }

    public LectureUser(User user, Lecture lecture) {
        assert (user != null);
        assert (lecture != null);
        this.user = user;
        this.lecture = lecture;
    }
}
