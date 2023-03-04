package jipdol2.eunstargram.exception;

public class MemberNotFound extends JipDol2Exception{

    private static final String MESSAGE = "등록된 회원을 찾을 수 없습니다.";

    public MemberNotFound(){
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
