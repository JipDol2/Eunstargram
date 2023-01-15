package jipdol2.eunstargram.comment.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class CommentSaveRequestDTO {

    private String content;

    private Long likeNumber;

    private Long postId;

    private Long memberId;
}
