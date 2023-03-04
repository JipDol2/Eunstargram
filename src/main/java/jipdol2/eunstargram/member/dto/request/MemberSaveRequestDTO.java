package jipdol2.eunstargram.member.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class MemberSaveRequestDTO {

    private String memberEmail;

    private String password;

    private String nickName;

    private String phoneNumber;

    private String birthDay;

    private String intro;

    @Builder
    public MemberSaveRequestDTO(String memberEmail, String password, String nickName, String phoneNumber, String birthDay, String intro) {
        this.memberEmail = memberEmail;
        this.password = password;
        this.nickName = nickName;
        this.phoneNumber = phoneNumber;
        this.birthDay = birthDay;
        this.intro = intro;
    }
}
