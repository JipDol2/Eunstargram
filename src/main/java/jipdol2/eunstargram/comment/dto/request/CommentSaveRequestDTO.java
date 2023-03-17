package jipdol2.eunstargram.comment.dto.request;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor
public class CommentSaveRequestDTO {

    private String content;

    private Long postId;

    @Builder
    public CommentSaveRequestDTO(String content, Long postId) {
        this.content = content;
        this.postId = postId;
    }
}
