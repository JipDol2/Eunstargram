package jipdol2.eunstargram.member.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberLoginDTO {

    private String memberId;

    private String password;
}
