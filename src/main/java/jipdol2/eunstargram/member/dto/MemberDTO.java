package jipdol2.eunstargram.member.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class MemberDTO {

    private String memberId;

    private String password;

    private String nickName;

    private String phoneNumber;

    private String birthDay;

    private String intro;

    private String imagePath;
}
