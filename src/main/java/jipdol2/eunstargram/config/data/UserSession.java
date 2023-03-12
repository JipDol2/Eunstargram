package jipdol2.eunstargram.config.data;

import lombok.Getter;

@Getter
public class UserSession {

    public Long id;

    public String email;

    public UserSession(Long id){
        this.id = id;
    }
}
