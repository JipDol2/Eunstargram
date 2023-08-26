package jipdol2.eunstargram.exception.auth;

import jipdol2.eunstargram.exception.BaseException;

public class MissAuthorized extends BaseException {

    private static final String CODE = "A002";

    private static final String MESSAGE = "권한이 일치하지 않습니다.";

    public MissAuthorized() {
        super(CODE,MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}
