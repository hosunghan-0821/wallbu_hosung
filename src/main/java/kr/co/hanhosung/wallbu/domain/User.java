package kr.co.hanhosung.wallbu.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter(AccessLevel.PROTECTED)
@Table(name = "tb_user")
public class User  extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    @Column(unique = true)
    private String phoneNumber;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    public User() {
    }

    public User(String name, String email, String phoneNumber, String password, UserRole userRole) {

        assert (name != null && !name.isEmpty());
        assert (email != null && !email.isEmpty());
        assert (phoneNumber != null);
        assert (password != null);
        assert (userRole != null);

        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.userRole = userRole;
    }
}
