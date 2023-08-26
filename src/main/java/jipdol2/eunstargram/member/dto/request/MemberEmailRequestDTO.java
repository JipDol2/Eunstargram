package jipdol2.eunstargram.member.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class MemberEmailRequestDTO {

    private String email;

    public MemberEmailRequestDTO(String email) {
        this.email = email;
    }
}
