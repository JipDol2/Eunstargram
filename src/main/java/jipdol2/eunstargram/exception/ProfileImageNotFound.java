package jipdol2.eunstargram.exception;

import java.util.Map;

public class ProfileImageNotFound extends JipDol2Exception{

    private final static String MESSAGE = "프로필 이미지를 찾을 수 없습니다.";

    public ProfileImageNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
