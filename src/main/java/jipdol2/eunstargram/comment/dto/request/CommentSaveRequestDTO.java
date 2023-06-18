package jipdol2.eunstargram.comment.dto.request;

import lombok.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Getter
@ToString
@NoArgsConstructor
public class CommentSaveRequestDTO {

    @NotBlank(message = "댓글을 입력해주세요")
    private String content;

    @NotNull(message = "postId 는 null 일 수 없습니다")
    private Long postId;

    @Builder
    public CommentSaveRequestDTO(String content, Long postId) {
        this.content = content;
        this.postId = postId;
    }
}
