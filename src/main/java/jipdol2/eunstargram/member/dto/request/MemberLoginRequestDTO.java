package jipdol2.eunstargram.member.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberLoginRequestDTO {

    private String memberId;

    private String password;

    @Override
    public String toString() {
        return "MemberLoginRequestDTO{" +
                "memberId='" + memberId + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
