package jipdol2.eunstargram.comment.entity;

import jipdol2.eunstargram.post.entity.Post;
import jipdol2.eunstargram.common.entity.BaseTimeEntity;
import jipdol2.eunstargram.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private String content;

    private Long likeNumber;

    @ManyToOne
    @JoinColumn(name = "POST_ID", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Post post;

    @ManyToOne
    @JoinColumn(name = "MEMEBER_ID", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Member member;

    @Builder
    public Comment(String content, Long likeNumber, Post post, Member member) {
        this.content = content;
        this.likeNumber = likeNumber;
        checkPost(post);
        checkMember(member);
    }

    private void checkPost(Post post) {
        if(this.post != null){
            this.post.getComments().remove(this);
        }
        this.post = post;
        post.getComments().add(this);
    }

    private void checkMember(Member member) {
        if(this.member != null){
            this.member.getComments().remove(this);
        }
        this.member = member;
        member.getComments().add(this);
    }


}
