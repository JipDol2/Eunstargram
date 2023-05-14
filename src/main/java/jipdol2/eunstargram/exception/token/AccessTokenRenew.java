package jipdol2.eunstargram.exception.token;

import jipdol2.eunstargram.exception.BaseException;

public class AccessTokenRenew extends BaseException {

    private static final String MESSAGE = "accessToken 재발급이 필요합니다";

    public AccessTokenRenew() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}
