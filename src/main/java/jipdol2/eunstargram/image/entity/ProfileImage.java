package jipdol2.eunstargram.image.entity;

import jipdol2.eunstargram.common.entity.BaseTimeEntity;
import jipdol2.eunstargram.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class ProfileImage extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private String originalFileName;

    private String storedFileName;

 /*   @OneToOne
    @JoinColumn(name = "MEMBER_ID", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Member member;*/

    @Builder
    public ProfileImage(String originalFileName, String storedFileName, Member member) {
        this.originalFileName = originalFileName;
        this.storedFileName = storedFileName;
//        this.member = member;
    }
}
