package jipdol2.eunstargram.exception.member;

import jipdol2.eunstargram.exception.BaseException;

public class MemberNotFound extends BaseException {

    private static final String CODE = "M001";
    private static final String MESSAGE = "등록된 회원이 아닙니다";

    public MemberNotFound(){
        super(CODE,MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
