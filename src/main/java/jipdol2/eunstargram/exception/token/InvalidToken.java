package jipdol2.eunstargram.exception.token;

import jipdol2.eunstargram.exception.BaseException;

public class InvalidToken extends BaseException {

    private static final String MESSAGE = "Token 이 유효하지 않습니다.";

    public InvalidToken() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
