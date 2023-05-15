package jipdol2.eunstargram.exception.member;

import jipdol2.eunstargram.exception.BaseException;

public class InvalidSignInInformation extends BaseException {

    private static final String CODE = "M004";
    private static final String MESSAGE = "아이디/비밀번호가 올바르지 않습니다";

    public InvalidSignInInformation() {
        super(CODE,MESSAGE);
    }

    public InvalidSignInInformation(String fieldName,String message){
        super(CODE,MESSAGE);
        addValidation(fieldName,message);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
