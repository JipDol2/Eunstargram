package jipdol2.eunstargram.comment.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Service;

@Getter @Setter
@ToString
public class CommentSaveRequestDTO {

    private String comment;

    private Long likeNumber;

    private Long postId;

    private Long memberId;
}
