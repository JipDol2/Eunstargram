package jipdol2.eunstargram.comment.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CommentFindResponseDTO {

    private Long id;

    private String content;

    private Long likeNumber;

    private String deleteYn;

    private String nickname;

    @Builder
    public CommentFindResponseDTO(Long id, String content, Long likeNumber, String deleteYn, String nickname) {
        this.id = id;
        this.content = content;
        this.likeNumber = likeNumber;
        this.deleteYn = deleteYn;
        this.nickname = nickname;
    }
}
