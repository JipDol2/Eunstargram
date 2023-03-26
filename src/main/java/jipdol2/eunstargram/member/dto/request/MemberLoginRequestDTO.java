package jipdol2.eunstargram.member.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class MemberLoginRequestDTO {

    private String memberEmail;

    private String password;

    @Builder
    public MemberLoginRequestDTO(String memberEmail, String password) {
        this.memberEmail = memberEmail;
        this.password = password;
    }
}
