package jipdol2.eunstargram.article.entity;

import jipdol2.eunstargram.comment.entity.Comment;
import jipdol2.eunstargram.member.entity.Member;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Article {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private String imagePath;

    private Long likeNumber;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Member member;

    @OneToMany(mappedBy = "article")
    private List<Comment> comments = new ArrayList<>();
}
