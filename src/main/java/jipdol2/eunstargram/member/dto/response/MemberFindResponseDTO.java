package jipdol2.eunstargram.member.dto.response;

import jipdol2.eunstargram.member.entity.Member;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor
public class MemberFindResponseDTO {

    private Long id;

    private String memberId;

    private String password;

    private String nickName;

    private String phoneNumber;

    private String birthDay;

    private String intro;

    private String imagePath;

    private String deleteYn;

    @Builder
    public MemberFindResponseDTO(Long id, String memberId, String password, String nickName, String phoneNumber, String birthDay, String intro, String imagePath, String deleteYn) {
        this.id = id;
        this.memberId = memberId;
        this.password = password;
        this.nickName = nickName;
        this.phoneNumber = phoneNumber;
        this.birthDay = birthDay;
        this.intro = intro;
        this.imagePath = imagePath;
        this.deleteYn = deleteYn;
    }

    public static MemberFindResponseDTO createMemberFindResponseDTO(Member member){
        return MemberFindResponseDTO.builder()
                .id(member.getId())
                .memberId(member.getMemberId())
                .password(member.getPassword())
                .nickName(member.getNickname())
                .phoneNumber(member.getPhoneNumber())
                .birthDay(member.getBirthDay())
                .intro(member.getIntro())
                .deleteYn(member.getDeleteYn())
                .build();
    }
}
