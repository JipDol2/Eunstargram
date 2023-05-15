package jipdol2.eunstargram.exception.auth;

import jipdol2.eunstargram.exception.BaseException;

public class AccessTokenRenew extends BaseException {

    private static final String CODE = "A003";
    private static final String MESSAGE = "accessToken 재발급이 필요합니다";

    public AccessTokenRenew() {
        super(CODE,MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}
