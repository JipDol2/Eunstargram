package jipdol2.eunstargram.image.entity;

import jipdol2.eunstargram.common.entity.BaseTimeEntity;
import jipdol2.eunstargram.post.entity.Post;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originalFileName;

    private String storedFileName;

    //enum type 은 항상 EnumType.STRING 으로 설정하자!
    @Enumerated(EnumType.STRING)
    private ImageCode imageCode;

//    @JsonIgnore : image entity를 직접 클라이언트에게 응답했었다. 그러나 무한순환참조로 인한 에러 발생
//    @JsonIgnore 어노테이션으로 해결할 수 있지만 좋지 못한 방법->DTO 를 생성해라
    @OneToOne(mappedBy = "image", fetch = FetchType.LAZY)
    private Post post;

    @Builder
    public Image(String originalFileName, String storedFileName, ImageCode imageCode, Post post) {
        this.originalFileName = originalFileName;
        this.storedFileName = storedFileName;
        this.imageCode = imageCode;
        /**
         * 단방향 연관관계 편의 메소드 구현
         */
        this.post = post;
    }
}
