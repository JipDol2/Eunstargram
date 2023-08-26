package jipdol2.eunstargram.member.dto.response;

import lombok.Getter;

@Getter
public class MemberValidationCheckEmailDTO {
    private boolean flag;

    public MemberValidationCheckEmailDTO(boolean flag) {
        this.flag = flag;
    }
}
