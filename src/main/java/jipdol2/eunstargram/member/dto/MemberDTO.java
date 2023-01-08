package jipdol2.eunstargram.member.dto;

import jipdol2.eunstargram.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberDTO {

    private String memberId;

    private String password;

    private String nickName;

    private String phoneNumber;

    private String birthDay;

    private String intro;

    private String imagePath;

    @Override
    public String toString() {
        return "MemberDTO{" +
                "memberId='" + memberId + '\'' +
                ", password='" + password + '\'' +
                ", nickName='" + nickName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", birthDay='" + birthDay + '\'' +
                ", intro='" + intro + '\'' +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }
/*    @Builder
    public MemberDTO(String memberId, String password, String nickName, String phoneNumber, String birthDay, String intro, String imagePath) {
        this.memberId = memberId;
        this.password = password;
        this.nickName = nickName;
        this.phoneNumber = phoneNumber;
        this.birthDay = birthDay;
        this.intro = intro;
        this.imagePath = imagePath;
    }*/
}
