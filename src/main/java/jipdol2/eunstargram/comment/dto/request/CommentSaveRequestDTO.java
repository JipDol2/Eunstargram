package jipdol2.eunstargram.comment.dto.request;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor
public class CommentSaveRequestDTO {

    private String content;

    private Long postId;

    private Long memberId;

    @Builder
    public CommentSaveRequestDTO(String content, Long postId, Long memberId) {
        this.content = content;
        this.postId = postId;
        this.memberId = memberId;
    }
}
