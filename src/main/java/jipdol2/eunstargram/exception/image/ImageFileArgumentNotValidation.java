package jipdol2.eunstargram.exception.image;

import jipdol2.eunstargram.exception.JipDol2Exception;

public class ImageFileArgumentNotValidation extends JipDol2Exception {

    private static final String MESSAGE = "이미지 파일이 존재하지 않습니다";

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
