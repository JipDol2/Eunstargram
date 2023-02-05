package jipdol2.eunstargram.post.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class PostResponseDTO {

    private Long id;

    private Long likeNumber;

    private String content;

    private Long memberId;

    //TODO ImageDTO 생성 후 여기에 dto 필드 추가 필요

    public PostResponseDTO(Long id, Long likeNumber, String content, Long memberId) {
        this.id = id;
        this.likeNumber = likeNumber;
        this.content = content;
        this.memberId = memberId;
    }
}
