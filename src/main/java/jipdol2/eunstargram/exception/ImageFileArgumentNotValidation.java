package jipdol2.eunstargram.exception;

public class ImageFileArgumentNotValidation extends JipDol2Exception{

    private static final String MESSAGE = "Bad Request";

    public ImageFileArgumentNotValidation() {
        super(MESSAGE);
    }

    public ImageFileArgumentNotValidation(String fieldName,String message){
        super(MESSAGE);
        addValidation(fieldName,message);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
