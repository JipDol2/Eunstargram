package jipdol2.eunstargram.comment.entity;

import jipdol2.eunstargram.article.entity.Article;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Comment {

    @Id @GeneratedValue
    private Long seq;

    private String content;

    private Long likeNumber;

    @ManyToOne
    @JoinColumn(name = "ARTICEL_ID", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Article article;
}
