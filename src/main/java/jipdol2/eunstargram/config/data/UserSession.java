package jipdol2.eunstargram.config.data;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserSession {

    public Long id;

    public String email;

    public String nickname;

    @Builder
    public UserSession(Long id,String email,String nickname){
        this.id = id;
        this.email = email;
        this.nickname = nickname;
    }
}
