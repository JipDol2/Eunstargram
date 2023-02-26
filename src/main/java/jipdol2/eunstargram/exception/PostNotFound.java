package jipdol2.eunstargram.exception;

/**
 * status -> 404
 */
public class PostNotFound extends JipDol2Exception{

    private static final String MESSAGE = "게시글을 찾을 수 없습니다.";

    public PostNotFound(){
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
