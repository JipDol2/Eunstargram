package jipdol2.eunstargram.post.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * 2023-02-22
 * 게시글 수정은 내용만 허용
 */
@Getter
@ToString
public class PostEditRequestDTO {

    private String content;

    public PostEditRequestDTO(){

    }

    @Builder
    public PostEditRequestDTO(String content) {
        this.content = content;
    }
}
