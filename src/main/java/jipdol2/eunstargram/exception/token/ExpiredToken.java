package jipdol2.eunstargram.exception.token;

import jipdol2.eunstargram.exception.BaseException;

public class ExpiredToken extends BaseException {

    private static final String MESSAGE = "토큰이 만료되었습니다.";

    public ExpiredToken() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}
