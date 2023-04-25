package jipdol2.eunstargram.exception.auth;

import jipdol2.eunstargram.exception.JipDol2Exception;

import java.util.Map;

public class MissAuthorized extends JipDol2Exception {

    private static final String MESSAGE = "권한이 일치하지 않습니다.";

    public MissAuthorized() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}
