package jipdol2.eunstargram.post.dto.response;

import jipdol2.eunstargram.image.dto.response.ImageResponseDTO;
import jipdol2.eunstargram.post.entity.Post;
import lombok.Builder;
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

    private String nickname;

    private ImageResponseDTO imageResponseDTO;

    @Builder
    public PostResponseDTO(Long id, Long likeNumber, String content, Long memberId, String nickname, ImageResponseDTO imageResponseDTO) {
        this.id = id;
        this.likeNumber = likeNumber;
        this.content = content;
        this.memberId = memberId;
        this.nickname = nickname;
        this.imageResponseDTO = imageResponseDTO;
    }

    public PostResponseDTO(Post post, ImageResponseDTO imageResponseDTO){
        this.id = post.getId();
        this.likeNumber = post.getLikeNumber();
        this.content = post.getContent();
        this.memberId = post.getMember().getId();
        this.nickname = post.getMember().getNickname();
        this.imageResponseDTO = imageResponseDTO;
    }
}
