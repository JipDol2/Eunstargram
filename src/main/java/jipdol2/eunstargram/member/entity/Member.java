package jipdol2.eunstargram.member.entity;

import jipdol2.eunstargram.article.entity.Article;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
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

    @Builder
    public Member(
            String userId,
            String password,
            String nickname,
            String phoneNumber,
            String birthDay,
            String intro,
            String imagePath
    ) {
        this.userId = userId;
        this.password = password;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.birthDay = birthDay;
        this.intro = intro;
        this.imagePath = imagePath;
    }
}
