package jipdol2.eunstargram.auth.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

//TODO: 추후에 redis + docker 도입시에 @Entity 를 제외해야함
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Id @GeneratedValue
    private Long id;

    private String refreshToken;

    private Long timeToLive;

    @Builder
    public RefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
