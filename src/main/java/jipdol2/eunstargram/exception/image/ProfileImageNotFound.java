package jipdol2.eunstargram.exception.image;

import jipdol2.eunstargram.exception.BaseException;

public class ProfileImageNotFound extends BaseException {

    private final static String CODE = "I002";
    private final static String MESSAGE = "프로필 이미지를 찾을 수 없습니다.";

    public ProfileImageNotFound() {
        super(CODE,MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
