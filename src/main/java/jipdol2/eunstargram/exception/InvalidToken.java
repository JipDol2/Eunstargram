package jipdol2.eunstargram.exception;

import java.util.Map;

public class InvalidToken extends JipDol2Exception{

    private static final String MESSAGE = "Token 이 유효하지 않습니다.";

    public InvalidToken() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
