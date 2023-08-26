package jipdol2.eunstargram.member;

import jipdol2.eunstargram.crypto.MyPasswordEncoder;
import jipdol2.eunstargram.exception.member.MemberNotFound;
import jipdol2.eunstargram.exception.member.ValidationDuplicateMemberEmail;
import jipdol2.eunstargram.exception.member.ValidationDuplicateMemberNickname;
import jipdol2.eunstargram.image.entity.Image;
import jipdol2.eunstargram.image.entity.ImageCode;
import jipdol2.eunstargram.image.entity.ImageJpaRepository;
import jipdol2.eunstargram.member.dto.request.MemberSaveRequestDTO;
import jipdol2.eunstargram.member.dto.request.MemberUpdateRequestDTO;
import jipdol2.eunstargram.member.dto.response.MemberFindResponseDTO;
import jipdol2.eunstargram.member.entity.Member;
import jipdol2.eunstargram.member.entity.MemberJpaRepository;
import jipdol2.eunstargram.member.entity.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class MemberServiceTest {

    //Service
    @Autowired private MemberService memberService;
    //Repository
    @Autowired private MemberRepository memberRepository;
    @Autowired private MemberJpaRepository memberJpaRepository;
    @Autowired private ImageJpaRepository imageJpaRepository;
    //MyPasswordEncoder
    @Autowired
    MyPasswordEncoder myPasswordEncoder;

    @BeforeEach
    void clean() {
        memberJpaRepository.deleteAll();
    }

    @Test
    @DisplayName("회원 가입 테스트 - 비밀번호 암호화 확인")
    void memberJoinTest() {

        //given
        Member member = createMember(
                "jipdol2@gmail.com",
                "1234",
                "Rabbit",
                "010-1111-2222",
                "1994-07-15",
                "안녕하세요.토끼입니다");

        //when
        memberRepository.save(member);

        Member findByMember = memberJpaRepository.findAll().get(0);
        //then
        assertThat(memberJpaRepository.count()).isEqualTo(1);

        assertThat(findByMember.getMemberEmail()).isEqualTo("jipdol2@gmail.com");
        assertThat(findByMember.getPassword()).isEqualTo(member.getPassword());
        assertThat(findByMember.getNickname()).isEqualTo("Rabbit");
        assertThat(findByMember.getPhoneNumber()).isEqualTo("010-1111-2222");
        assertThat(findByMember.getBirthDay()).isEqualTo("1994-07-15");
        assertThat(findByMember.getIntro()).isEqualTo("안녕하세요.토끼입니다");
    }

    @Test
    @DisplayName("회원 가입 테스트 - 이메일 중복체크")
    void memberJoinDuplicateMemberEmailTest() {

        //given
        Member memberRabbit = createMember(
                "jipdol2@gmail.com",
                "1234",
                "Rabbit",
                "010-1111-2222",
                "1994-07-15",
                "안녕하세요.토끼입니다");

        MemberSaveRequestDTO memberTurtle = createMemberSaveRequestDTO(
                "jipdol2@gmail.com",
                "1234",
                "Turtle",
                "010-3333-4444",
                "1994-01-22",
                "안녕하세요.거북이입니다"
        );

        //when
        memberRepository.save(memberRabbit);

        //then
        assertThatThrownBy(() -> memberService.join(memberTurtle))
                .isInstanceOf(ValidationDuplicateMemberEmail.class)
                .hasMessage("중복된 이메일이 존재합니다.");
    }

    @Test
    @DisplayName("회원 가입 테스트 - 닉네임 중복체크")
    void memberJoinDuplicateMemberNicknameTest() {

        //given
        Member memberRabbit = createMember(
                "jipdol2@gmail.com",
                "1234",
                "Rabbit",
                "010-1111-2222",
                "1994-07-15",
                "안녕하세요.토끼입니다");

        MemberSaveRequestDTO memberTurtle = createMemberSaveRequestDTO(
                "eunseo@gmail.com",
                "1234",
                "Rabbit",
                "010-3333-4444",
                "1994-01-22",
                "안녕하세요.거북이입니다"
        );

        //when
        Long save = memberRepository.save(memberRabbit);
        //then
        assertThatThrownBy(() -> memberService.join(memberTurtle))
                .isInstanceOf(ValidationDuplicateMemberNickname.class)
                .hasMessage("중복된 닉네임이 존재합니다.");
    }

    @Test
    @DisplayName("회원 조회 테스트 - 이메일")
    void findByMemberEmail() throws Exception{
        //given
        Member memberRabbit = createMember(
                "jipdol2@gmail.com",
                "1234",
                "Rabbit",
                "010-1111-2222",
                "1994-07-15",
                "안녕하세요.토끼입니다");

        //when
        memberRepository.save(memberRabbit);
        //then
        MemberFindResponseDTO findByRabbit = memberService.findByMemberEmail(memberRabbit.getMemberEmail());
        assertThat(memberRabbit.getMemberEmail()).isEqualTo(findByRabbit.getMemberEmail());
        assertThat(memberRabbit.getNickname()).isEqualTo(findByRabbit.getNickname());
        assertThat(memberRabbit.getPhoneNumber()).isEqualTo(findByRabbit.getPhoneNumber());
        assertThat(memberRabbit.getBirthDay()).isEqualTo(findByRabbit.getBirthDay());
        assertThat(memberRabbit.getIntro()).isEqualTo(findByRabbit.getIntro());
    }

    @Test
    @DisplayName("회원 조회 테스트 - 닉네임")
    void findByMemberNickname() throws Exception{
        //given
        Member memberRabbit = createMember(
                "jipdol2@gmail.com",
                "1234",
                "Rabbit",
                "010-1111-2222",
                "1994-07-15",
                "안녕하세요.토끼입니다");

        //when
        memberRepository.save(memberRabbit);
        //then
        MemberFindResponseDTO findByRabbit = memberService.findByMemberNickname("Rabbit");
        assertThat(memberRabbit.getMemberEmail()).isEqualTo(findByRabbit.getMemberEmail());
        assertThat(memberRabbit.getNickname()).isEqualTo(findByRabbit.getNickname());
        assertThat(memberRabbit.getPhoneNumber()).isEqualTo(findByRabbit.getPhoneNumber());
        assertThat(memberRabbit.getBirthDay()).isEqualTo(findByRabbit.getBirthDay());
        assertThat(memberRabbit.getIntro()).isEqualTo(findByRabbit.getIntro());
    }

    @Test
    @DisplayName("회원 수정 테스트 - 비밀번호(암호화)")
    void updateMember() throws Exception{
        //given
        Member memberRabbit = createMember(
                "jipdol2@gmail.com",
                "1234",
                "Rabbit",
                "010-1111-2222",
                "1994-07-15",
                "안녕하세요.토끼입니다");

        Long id = memberRepository.save(memberRabbit);
        //when
        MemberUpdateRequestDTO memberUpdateRequestDTO = createMemberUpdateRequestDTO(
                "4321",
                "010-1111-222",
                "1994-07-15",
                "안녕하세요. 수정된 토끼입니다.");
        memberService.update(id,memberUpdateRequestDTO);
        //then
        Member member = memberRepository.findByOne(id)
                .orElseThrow(() -> new MemberNotFound());

        boolean matcher = myPasswordEncoder.matcher("4321", member.getPassword());
        assertThat(matcher).isTrue();
    }

    private Member createMember(
            String email,
            String password,
            String nickname,
            String phoneNumber,
            String birthDay,
            String intro
    ) {
        MyPasswordEncoder encoder = new MyPasswordEncoder();
        String encryptPassword = encoder.encrypt(password);

        Member member = Member.builder()
                .memberEmail(email)
                .password(encryptPassword)
                .nickname(nickname)
                .phoneNumber(phoneNumber)
                .birthDay(birthDay)
                .intro(intro)
                .build();
        return member;
    }

    private MemberSaveRequestDTO createMemberSaveRequestDTO(
            String email,
            String password,
            String nickname,
            String phoneNumber,
            String birthDay,
            String intro
    ) {
        MyPasswordEncoder encoder = new MyPasswordEncoder();
        String encryptPassword = encoder.encrypt(password);

        MemberSaveRequestDTO memberSaveRequestDTO = MemberSaveRequestDTO.builder()
                .memberEmail(email)
                .password(encryptPassword)
                .nickname(nickname)
                .intro(intro)
                .phoneNumber(phoneNumber)
                .birthDay(birthDay)
                .build();
        return memberSaveRequestDTO;
    }

    private MemberUpdateRequestDTO createMemberUpdateRequestDTO(
            String password,
            String phoneNumber,
            String birthDay,
            String intro
    ){
        MemberUpdateRequestDTO memberUpdateRequestDTO = MemberUpdateRequestDTO.builder()
                .password(password)
                .phoneNumber(phoneNumber)
                .birthDay(birthDay)
                .intro(intro)
                .build();
        return memberUpdateRequestDTO;
    }

    private Image createImage(Member member){

        String originalFileName = "testImage.jpg";

        String uuid = UUID.randomUUID().toString();
        String imageName = uuid + "_" + originalFileName;

        Image image = Image.builder()
                .originalFileName(originalFileName)
                .storedFileName(imageName)
                .imageCode(ImageCode.PROFILE)
                .build();
        return image;
    }

}