package jipdol2.eunstargram.member.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class MemberUpdateRequestDTO {

    private Long seq;

    private String password;

    private String nickName;

    private String phoneNumber;

    private String birthDay;

    private String intro;

    private String imagePath;

    private String deleteYn;

    @Builder
    public MemberUpdateRequestDTO(Long seq, String password, String nickName, String phoneNumber, String birthDay, String intro, String imagePath, String deleteYn) {
        this.seq = seq;
        this.password = password;
        this.nickName = nickName;
        this.phoneNumber = phoneNumber;
        this.birthDay = birthDay;
        this.intro = intro;
        this.imagePath = imagePath;
        this.deleteYn = deleteYn;
    }
}
