package jipdol2.eunstargram.member.entity;

import jipdol2.eunstargram.article.entity.Article;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private String userId;

    private String password;

    private String nickname;

    private String phoneNumber;

    private String birthDay;

    private String intro;

    private String imagePath;

    @OneToMany(mappedBy = "member")
    private List<Article> articles = new ArrayList<>();
}
