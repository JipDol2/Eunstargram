package jipdol2.eunstargram.auth.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginCheckDTO {

    private boolean authState;

    @Builder
    public LoginCheckDTO(boolean authState) {
        this.authState = authState;
    }
}
