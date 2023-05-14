package jipdol2.eunstargram.exception.post;

import jipdol2.eunstargram.exception.BaseException;

/**
 * status -> 404
 */
public class PostNotFound extends BaseException {

    private static final String MESSAGE = "게시글이 존재하지 않습니다";

    public PostNotFound(){
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
