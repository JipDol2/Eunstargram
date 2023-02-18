package jipdol2.eunstargram.image.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jipdol2.eunstargram.common.entity.BaseTimeEntity;
import jipdol2.eunstargram.member.entity.Member;
import jipdol2.eunstargram.post.entity.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Image extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originalFileName;

    private String storedFileName;

    private ImageCode imageCode;

//    @JsonIgnore : image entity를 직접 클라이언트에게 응답했었다. 그러나 무한순환참조로 인한 에러 발생
//    @JsonIgnore 어노테이션으로 해결할 수 있지만 좋지 못한 방법->DTO 를 생성해라
    @ManyToOne
    @JoinColumn(name = "MEMBER_ID", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Member member;

    @OneToOne(mappedBy = "image")
//    @JoinColumn(name = "POST_ID", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Post post;

    @Builder
    public Image(String originalFileName, String storedFileName, ImageCode imageCode, Member member ,Post post) {
        this.originalFileName = originalFileName;
        this.storedFileName = storedFileName;
        this.imageCode = imageCode;
        /**
         * 양방향 연관관계 편의 메소드 구현
         */
        if(this.member!=null){
            this.member.getImage().remove(this);
        }
        this.member = member;
        member.getImage().add(this);

        /**
         * 단방향 연관관계 편의 메소드 구현
         */
        this.post = post;
    }
}
