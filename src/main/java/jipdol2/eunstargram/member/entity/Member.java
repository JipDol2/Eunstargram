package jipdol2.eunstargram.member.entity;

import jipdol2.eunstargram.article.entity.Post;
import jipdol2.eunstargram.comment.entity.Comment;
import jipdol2.eunstargram.common.entity.BaseTimeEntity;
import jipdol2.eunstargram.member.dto.request.MemberUpdateRequestDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Member extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private String memberId;

    private String password;

    private String nickname;

    private String phoneNumber;

    private String birthDay;

    private String intro;

    private String imagePath;

    private String deleteYn;

    @OneToMany(mappedBy = "member")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "member")
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
            String deleteYn
    ) {
        this.memberId = memberId;
        this.password = password;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.birthDay = birthDay;
        this.intro = intro;
        this.imagePath = imagePath;
        this.deleteYn = deleteYn;
    }

    public void changePassword(String password){
        this.password = password;
    }

    public void changeNickName(String nickname){
        this.nickname = nickname;
    }

    public void changePhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }

    public void changeBirthDay(String birthDay){
        this.birthDay = birthDay;
    }

    public void changeIntro(String intro){
        this.intro = intro;
    }

    public void changeImagePath(String imagePath){
        this.imagePath = imagePath;
    }

    public void changeDeleteYn(String deleteYn){
        this.deleteYn = deleteYn;
    }

    public void changeMember(MemberUpdateRequestDTO updateRequestDTO){
        this.password = updateRequestDTO.getPassword();
        this.nickname = updateRequestDTO.getNickName();
        this.phoneNumber = updateRequestDTO.getPhoneNumber();
        this.birthDay = updateRequestDTO.getBirthDay();
        this.intro = updateRequestDTO.getIntro();
        this.imagePath = updateRequestDTO.getImagePath();
        this.deleteYn = updateRequestDTO.getDeleteYn();
    }
}
