package jipdol2.eunstargram.member.entity;

import jipdol2.eunstargram.article.entity.Post;
import jipdol2.eunstargram.comment.entity.Comment;
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

    private String memberId;

    private String password;

    private String nickname;

    private String phoneNumber;

    private String birthDay;

    private String intro;

    private String imagePath;

    private String cancelYN;

    @OneToMany(mappedBy = "member")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "comment")
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Member(
            String memberId,
            String password,
            String nickname,
            String phoneNumber,
            String birthDay,
            String intro,
            String imagePath,
            String cancelYN
    ) {
        this.memberId = memberId;
        this.password = password;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.birthDay = birthDay;
        this.intro = intro;
        this.imagePath = imagePath;
        this.cancelYN = cancelYN;
    }
}
