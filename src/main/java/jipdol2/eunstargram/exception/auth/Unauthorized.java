package jipdol2.eunstargram.exception.auth;

import jipdol2.eunstargram.exception.BaseException;

/**
 * status -> 401
 */
public class Unauthorized extends BaseException {

    private static final String CODE = "A002";

    private static final String MESSAGE = "인증이 필요합니다.";

    public Unauthorized(){
        super(CODE,MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}
