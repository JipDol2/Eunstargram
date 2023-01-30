package jipdol2.eunstargram.member.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberLoginResponseDTO {

    private String token;

    private Long id;

    @Builder
    public MemberLoginResponseDTO(String token, Long id) {
        this.token = token;
        this.id = id;
    }
}
