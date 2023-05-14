package jipdol2.eunstargram.exception.member;

import jipdol2.eunstargram.exception.BaseException;

public class InvalidSignInInformation extends BaseException {

    private static final String MESSAGE = "Bad Request";

    public InvalidSignInInformation() {
        super(MESSAGE);
    }

    public InvalidSignInInformation(String fieldName,String message){
        super(MESSAGE);
        addValidation(fieldName,message);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
