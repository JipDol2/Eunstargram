package jipdol2.eunstargram.comment.entity;

import jipdol2.eunstargram.article.entity.Article;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Comment {

    @Id @GeneratedValue
    private Long seq;

    private String content;

    private Long likeNumber;

    @ManyToOne
    @JoinColumn(name = "ARTICEL_ID", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Article article;

    @Builder
    public Comment(String content, Long likeNumber, Article article) {
        this.content = content;
        this.likeNumber = likeNumber;
        if(this.article != null){
            this.article.getComments().remove(this);
        }
        this.article = article;
        article.getComments().add(this);
    }
}
