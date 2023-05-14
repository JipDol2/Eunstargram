package jipdol2.eunstargram.exception.member;

import jipdol2.eunstargram.exception.BaseException;

public class ValidationDuplicateMemberNickname extends BaseException {

    private static final String MESSAGE = "중복된 닉네임이 존재합니다.";

    public ValidationDuplicateMemberNickname() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 409;
    }
}
