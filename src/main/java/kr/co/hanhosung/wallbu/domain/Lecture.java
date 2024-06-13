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
@Table(name = "tb_lecture")
public class Lecture extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private Long maxStudentCount;

    private Long price;

    private Long studentCount;

    @ManyToOne(fetch = FetchType.LAZY)
    private User createUser;

    public Lecture() {
    }

    public Lecture(String title, Long maxStudentCount, Long price, User createUser) {

        assert (title != null && !title.isEmpty());
        assert (maxStudentCount != null && maxStudentCount > 0);
        assert (price != null && price > 0);
        assert (createUser != null);

        this.title = title;
        this.maxStudentCount = maxStudentCount;
        this.price = price;
        this.createUser = createUser;
        this.studentCount = 0L;
    }

    public void upStudentCount() {
        this.studentCount++;
    }
}
