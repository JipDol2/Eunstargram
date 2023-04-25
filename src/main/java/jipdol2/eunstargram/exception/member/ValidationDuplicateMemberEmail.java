package jipdol2.eunstargram.exception.member;

import jipdol2.eunstargram.exception.JipDol2Exception;

public class ValidationDuplicateMemberEmail extends JipDol2Exception {

    private static final String MESSAGE = "중복된 이메일이 존재합니다.";

    public ValidationDuplicateMemberEmail() {
        super(MESSAGE);
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
