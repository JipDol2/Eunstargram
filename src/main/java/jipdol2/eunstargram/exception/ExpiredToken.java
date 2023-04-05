package jipdol2.eunstargram.exception;

public class ExpiredToken extends JipDol2Exception{

    private static final String MESSAGE = "토큰이 만료되었습니다.";

    public ExpiredToken() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
