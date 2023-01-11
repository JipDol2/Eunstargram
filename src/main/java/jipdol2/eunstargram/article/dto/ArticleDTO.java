package jipdol2.eunstargram.article.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class ArticleDTO {

    private String imagePath;

    private Long likeNumber;

    private String content;
}
