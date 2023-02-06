package jipdol2.eunstargram.post.dto.response;

import jipdol2.eunstargram.image.dto.ImageDTO;
import jipdol2.eunstargram.post.entity.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.PostPersist;

@Getter @Setter
@ToString
public class PostResponseDTO {

    private Long id;

    private Long likeNumber;

    private String content;

    private Long memberId;

    private ImageDTO imageDTO;

    @Builder
    public PostResponseDTO(Long id, Long likeNumber, String content, Long memberId, ImageDTO imageDTO) {
        this.id = id;
        this.likeNumber = likeNumber;
        this.content = content;
        this.memberId = memberId;
        this.imageDTO = imageDTO;
    }
    public PostResponseDTO(Post post,ImageDTO imageDTO){
        this.id = post.getId();
        this.likeNumber = post.getLikeNumber();
        this.content = post.getContent();
        this.memberId = post.getMember().getId();
        this.imageDTO = imageDTO;
    }
}
