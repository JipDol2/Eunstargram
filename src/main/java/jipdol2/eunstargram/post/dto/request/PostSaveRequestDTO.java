package jipdol2.eunstargram.post.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PostSaveRequestDTO {

    private Long likeNumber;

    private String content;

    private Long memberId;

    /**
     * json 형태의 데이터를 객체형태로 변환하는데 JackSon Library 가 사용된다.
     * 이때, 객체에는 '기본 생성자' 가 반드시 존재해야한다.
     * 이유는 JackSon Library가 기본생성자를 이용하여 객체로 변환시켜주기 때문이다.
     */
    public PostSaveRequestDTO(){
    }

    @Builder
    public PostSaveRequestDTO(Long likeNumber, String content, Long memberId) {
        this.likeNumber = likeNumber;
        this.content = content;
        this.memberId = memberId;
    }
}
