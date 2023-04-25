package jipdol2.eunstargram.exception.auth;

import jipdol2.eunstargram.exception.JipDol2Exception;

/**
 * status -> 401
 */
public class Unauthorized extends JipDol2Exception {

    private static final String MESSAGE = "인증이 필요합니다.";

    public Unauthorized(){
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}
