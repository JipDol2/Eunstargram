package jipdol2.eunstargram.member.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class MemberLoginRequestDTO {

    private String memberId;

    private String password;

    @Builder
    public MemberLoginRequestDTO(String memberId, String password) {
        this.memberId = memberId;
        this.password = password;
    }
}
