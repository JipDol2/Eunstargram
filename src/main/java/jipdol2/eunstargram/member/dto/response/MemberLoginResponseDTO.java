package jipdol2.eunstargram.member.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberLoginResponseDTO {

    private String token;

    private Long seq;

    @Builder
    public MemberLoginResponseDTO(String token, Long seq) {
        this.token = token;
        this.seq = seq;
    }
}
