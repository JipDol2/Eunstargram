package jipdol2.eunstargram.member.dto.response;

import jipdol2.eunstargram.member.entity.Member;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor
public class MemberFindResponseDTO {

    private Long seq;

    private String memberId;

    private String password;

    private String nickName;

    private String phoneNumber;

    private String birthDay;

    private String intro;

    private String imagePath;

    private String deleteYn;

    @Builder
    public MemberFindResponseDTO(Long seq, String memberId, String password, String nickName, String phoneNumber, String birthDay, String intro, String imagePath, String deleteYn) {
        this.seq = seq;
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
                .seq(member.getSeq())
                .memberId(member.getMemberId())
                .password(member.getPassword())
                .nickName(member.getNickname())
                .phoneNumber(member.getPhoneNumber())
                .birthDay(member.getBirthDay())
                .intro(member.getIntro())
                .imagePath(member.getImagePath())
                .deleteYn(member.getDeleteYn())
                .build();
    }
}