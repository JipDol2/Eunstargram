package jipdol2.eunstargram.post.entity;

import jipdol2.eunstargram.comment.entity.Comment;
import jipdol2.eunstargram.common.entity.BaseTimeEntity;
import jipdol2.eunstargram.image.entity.Image;
import jipdol2.eunstargram.member.entity.Member;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Post extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    private String imagePath;

    private Long likeNumber;

    private String content;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Member member;

    /**
     * @Builder.Default 를 붙이는 이유
     * 링크 : https://velog.io/@shining_dr/Builder-%ED%8C%A8%ED%84%B4%EA%B3%BC-NullPointException
     */
    @OneToMany(mappedBy = "post")
//    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @OneToOne(mappedBy = "post")
    private Image image;

    @Builder
    public Post(Long likeNumber, String content, Member member,Image image) {
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

        this.image = image;
    }

    public void changeImage(Image image){
        this.image = image;
    }

}
