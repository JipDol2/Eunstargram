package jipdol2.eunstargram.config.data;

import lombok.Getter;

@Getter
public class UserSession {

    public Long id;

    public String name;

    public UserSession(Long id){
        this.id = id;
    }
}
