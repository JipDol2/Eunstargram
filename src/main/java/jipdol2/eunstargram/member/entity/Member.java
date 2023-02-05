package jipdol2.eunstargram.member.entity;

import jipdol2.eunstargram.image.entity.Image;
import jipdol2.eunstargram.post.entity.Post;
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
    private Long id;

    private String memberId;

    private String password;

    private String nickname;

    private String phoneNumber;

    private String birthDay;

    private String intro;

    private String deleteYn;

    @OneToMany(mappedBy = "member")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Image> image = new ArrayList<>();

    @Builder
    public Member(
            String memberId,
            String password,
            String nickname,
            String phoneNumber,
            String birthDay,
            String intro,
            String deleteYn
    ) {
        this.memberId = memberId;
        this.password = password;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.birthDay = birthDay;
        this.intro = intro;
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

    public void changeDeleteYn(String deleteYn){
        this.deleteYn = deleteYn;
    }

    public void changeMember(MemberUpdateRequestDTO updateRequestDTO){
        this.password = updateRequestDTO.getPassword();
        this.nickname = updateRequestDTO.getNickName();
        this.phoneNumber = updateRequestDTO.getPhoneNumber();
        this.birthDay = updateRequestDTO.getBirthDay();
        this.intro = updateRequestDTO.getIntro();
        this.deleteYn = updateRequestDTO.getDeleteYn();
    }
}
