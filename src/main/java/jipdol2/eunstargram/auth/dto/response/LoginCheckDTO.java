package jipdol2.eunstargram.auth.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginCheckDTO {

    private boolean authState;

    public LoginCheckDTO(boolean authState) {
        this.authState = authState;
    }

    public static LoginCheckDTO of(Boolean authState){
        return new LoginCheckDTO(authState);
    }
}
