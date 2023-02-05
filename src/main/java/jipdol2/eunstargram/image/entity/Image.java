package jipdol2.eunstargram.image.entity;

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

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Member member;

    @OneToOne
    @JoinColumn(name = "POST_ID", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
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
