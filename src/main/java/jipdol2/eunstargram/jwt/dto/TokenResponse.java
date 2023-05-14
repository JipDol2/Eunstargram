package jipdol2.eunstargram.jwt.dto;

import lombok.Getter;

@Getter
public class TokenResponse {

    private String accessToken;

    public TokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public static TokenResponse of(String accessToken){
        return new TokenResponse(accessToken);
    }
}
