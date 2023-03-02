package jipdol2.eunstargram.auth.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class LoginRequestDTO {

    private String memberEmail;

    private String password;

    @Builder
    public LoginRequestDTO(String memberEmail, String password) {
        this.memberEmail = memberEmail;
        this.password = password;
    }
}
