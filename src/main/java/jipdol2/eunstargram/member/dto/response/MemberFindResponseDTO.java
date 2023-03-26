package jipdol2.eunstargram.member.dto.response;

import jipdol2.eunstargram.member.entity.Member;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor
public class MemberFindResponseDTO {

    private Long id;

    private String memberEmail;

    private String password;

    private String nickname;

    private String phoneNumber;

    private String birthDay;

    private String intro;

    private String deleteYn;

    @Builder
    public MemberFindResponseDTO(Long id, String memberEmail, String password, String nickname, String phoneNumber, String birthDay, String intro, String deleteYn) {
        this.id = id;
        this.memberEmail = memberEmail;
        this.password = password;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.birthDay = birthDay;
        this.intro = intro;
        this.deleteYn = deleteYn;
    }

    public static MemberFindResponseDTO createMemberFindResponseDTO(Member member){
        return MemberFindResponseDTO.builder()
                .id(member.getId())
                .memberEmail(member.getMemberEmail())
                .password(member.getPassword())
                .nickname(member.getNickname())
                .phoneNumber(member.getPhoneNumber())
                .birthDay(member.getBirthDay())
                .intro(member.getIntro())
                .deleteYn(member.getDeleteYn())
                .build();
    }
}
