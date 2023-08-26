package jipdol2.eunstargram.exception.auth;

import jipdol2.eunstargram.exception.BaseException;

public class InvalidToken extends BaseException {

    private static final String CODE = "A005";
    private static final String MESSAGE = "Token 이 유효하지 않습니다.";

    public InvalidToken() {
        super(CODE,MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
