package jipdol2.eunstargram.jwt.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserSessionDTO {

    private Long id;

    private String email;

    private String nickname;

    @Builder
    public UserSessionDTO(Long id, String email, String nickname) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
    }
}
