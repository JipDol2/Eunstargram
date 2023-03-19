package jipdol2.eunstargram.exception;

public class InvalidSignInInformation extends JipDol2Exception{

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
