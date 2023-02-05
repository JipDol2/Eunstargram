package jipdol2.eunstargram.image.entity;

import jipdol2.eunstargram.common.entity.BaseTimeEntity;
import jipdol2.eunstargram.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class PostImage extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originalFileName;

    private String storedFileName;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Member member;

    @Builder
    public PostImage(String originalFileName, String storedFileName, Member member) {
        this.originalFileName = originalFileName;
        this.storedFileName = storedFileName;
        /**
         * 양방향 연관관계 편의 메소드 구현
         */
        if(this.member!=null){
            this.member.getPostImages().remove(this);
        }
        this.member = member;
        member.getPostImages().add(this);
    }
}
