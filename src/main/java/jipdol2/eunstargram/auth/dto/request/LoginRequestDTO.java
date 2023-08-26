package jipdol2.eunstargram.auth.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class LoginRequestDTO {

//    @Email(message = "올바른 이메일 형식을 입력해주세요")
    private String memberEmail;

//    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;

    private String code;

    @Builder
    public LoginRequestDTO(String memberEmail, String password,String code) {
        this.memberEmail = memberEmail;
        this.password = password;
        this.code = code;
    }
}
