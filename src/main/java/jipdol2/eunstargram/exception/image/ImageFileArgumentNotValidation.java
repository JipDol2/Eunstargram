package jipdol2.eunstargram.exception.image;

import jipdol2.eunstargram.exception.BaseException;

public class ImageFileArgumentNotValidation extends BaseException {

    private static final String CODE = "I001";
    private static final String MESSAGE = "이미지 파일이 존재하지 않습니다";

    public ImageFileArgumentNotValidation() {
        super(CODE,MESSAGE);
    }

    public ImageFileArgumentNotValidation(String fieldName,String message){
        super(CODE,MESSAGE);
        addValidation(fieldName,message);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
