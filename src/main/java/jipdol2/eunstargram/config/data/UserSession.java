package jipdol2.eunstargram.config.data;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserSession {

    public Long id;

    @Builder
    public UserSession(Long id){
        this.id = id;
    }
}
