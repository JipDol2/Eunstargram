package jipdol2.eunstargram.post.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class PostDTO {

    private Long articleId;

    private String imagePath;

    private Long likeNumber;

    private String content;

    private Long memberId;

    public PostDTO(Long articleId, String imagePath, Long likeNumber, String content, Long memberId) {
        this.articleId = articleId;
        this.imagePath = imagePath;
        this.likeNumber = likeNumber;
        this.content = content;
        this.memberId = memberId;
    }
}
