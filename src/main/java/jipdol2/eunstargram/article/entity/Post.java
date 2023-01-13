package jipdol2.eunstargram.article.entity;

import jipdol2.eunstargram.comment.entity.Comment;
import jipdol2.eunstargram.member.entity.Member;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private String imagePath;

    private Long likeNumber;

    private String content;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Member member;

    /**
     * @Builder.Default 를 붙이는 이유
     * 링크 : https://velog.io/@shining_dr/Builder-%ED%8C%A8%ED%84%B4%EA%B3%BC-NullPointException
     */
    @OneToMany(mappedBy = "article")
//    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Post(String imagePath, Long likeNumber, String content, Member member) {
        this.imagePath = imagePath;
        this.likeNumber = likeNumber;
        this.content = content;
        /**
         * 양방향 연관관계 편의 메소드 구현
         */
        if(this.member != null){
            this.member.getPosts().remove(this);
        }
        this.member = member;
        member.getPosts().add(this);
    }

}
