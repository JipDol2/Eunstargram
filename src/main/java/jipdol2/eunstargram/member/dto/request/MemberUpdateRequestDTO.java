package jipdol2.eunstargram.member.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class MemberUpdateRequestDTO {

    private Long id;

    private String password;

    private String phoneNumber;

    private String birthDay;

    private String intro;

    private String imagePath;

    private String deleteYn;

    @Builder
    public MemberUpdateRequestDTO(Long id, String password, String phoneNumber, String birthDay, String intro, String imagePath, String deleteYn) {
        this.id = id;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.birthDay = birthDay;
        this.intro = intro;
        this.imagePath = imagePath;
        this.deleteYn = deleteYn;
    }
}
