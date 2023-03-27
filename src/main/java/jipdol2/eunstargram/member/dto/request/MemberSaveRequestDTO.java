package jipdol2.eunstargram.member.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@ToString
@NoArgsConstructor
public class MemberSaveRequestDTO {

    @Email(message = "올바른 이메일 형식을 적어주세요")
    private String memberEmail;

    @NotBlank(message = "비밀번호를 적어주세요")
    private String password;

    @NotBlank(message = "닉네임을 적어주세요")
    private String nickname;

    private String phoneNumber;

    private String birthDay;

    private String intro;

    @Builder
    public MemberSaveRequestDTO(String memberEmail, String password, String nickname, String phoneNumber, String birthDay, String intro) {
        this.memberEmail = memberEmail;
        this.password = password;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.birthDay = birthDay;
        this.intro = intro;
    }
}
