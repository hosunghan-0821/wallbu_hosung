package kr.co.hanhosung.wallbu.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter(AccessLevel.PROTECTED)
@Table(name = "tb_token")
public class RefreshToken extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String refreshToken;

    public RefreshToken() {
    }

    public RefreshToken(Long userId, String refreshToken) {

        assert (userId != null && userId > 0);
        assert (refreshToken != null && !refreshToken.isEmpty());

        this.userId = userId;
        this.refreshToken = refreshToken;
    }
}
