package jipdol2.eunstargram.exception;

public class MemberNotFound extends JipDol2Exception{

    private static final String MESSAGE = "등록된 회원이 아닙니다";

    public MemberNotFound(){
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
