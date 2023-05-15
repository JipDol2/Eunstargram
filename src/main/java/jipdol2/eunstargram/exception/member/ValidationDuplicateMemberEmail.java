package jipdol2.eunstargram.exception.member;

import jipdol2.eunstargram.exception.BaseException;

public class ValidationDuplicateMemberEmail extends BaseException {

    private static final String CODE = "M002";
    private static final String MESSAGE = "중복된 이메일이 존재합니다.";

    public ValidationDuplicateMemberEmail() {
        super(CODE,MESSAGE);
    }

    /**
     * 409 status code : conflict
     * @return
     */
    @Override
    public int getStatusCode() {
        return 409;
    }
}
