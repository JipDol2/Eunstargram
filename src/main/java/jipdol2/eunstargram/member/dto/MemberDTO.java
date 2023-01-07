package jipdol2.eunstargram.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberDTO {

    private String userId;

    private String password;

    private String nickName;

    private String phoneNumber;

    private String birthDay;

    private String intro;

    private String imagePath;
}
