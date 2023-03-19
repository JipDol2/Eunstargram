package jipdol2.eunstargram.auth.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@ToString
@NoArgsConstructor
public class LoginRequestDTO {

    @Email(message = "올바른 이메일 형식을 입력해주세요")
    private String memberEmail;

    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;

    @Builder
    public LoginRequestDTO(String memberEmail, String password) {
        this.memberEmail = memberEmail;
        this.password = password;
    }
}
