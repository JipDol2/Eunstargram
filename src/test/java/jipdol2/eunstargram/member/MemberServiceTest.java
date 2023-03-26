package jipdol2.eunstargram.member;

import jipdol2.eunstargram.crypto.PasswordEncoder;
import jipdol2.eunstargram.member.dto.request.MemberSaveRequestDTO;
import jipdol2.eunstargram.member.entity.Member;
import jipdol2.eunstargram.member.entity.MemberJpaRepository;
import jipdol2.eunstargram.member.entity.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class MemberServiceTest {

    //Service
    @Autowired
    private MemberService memberService;
    //Repository
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberJpaRepository memberJpaRepository;

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
    @DisplayName("회원 가입 테스트 - 중복체크")
    void memberJoinDuplicateTest() {

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
        )

                ;MemberSaveRequestDTO.builder()
                .memberEmail("jipdol2@gmail.com")
                .password("1234")
                .nickname("Turtle")
                .phoneNumber("010-3333-4444")
                .birthDay("1994-01-22")
                .intro("안녕하세요.거북이입니다")
                .build();
        //when
        memberRepository.save(memberRabbit);
        //then
        assertThatThrownBy(() -> memberService.join(memberTurtle))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 존재하는 회원입니다");
    }

    private Member createMember(
            String email,
            String password,
            String nickname,
            String phoneNumber,
            String birthDay,
            String intro
    ) {
        PasswordEncoder encoder = new PasswordEncoder();
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
        PasswordEncoder encoder = new PasswordEncoder();
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
}