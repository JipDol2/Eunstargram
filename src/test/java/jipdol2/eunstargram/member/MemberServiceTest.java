package jipdol2.eunstargram.member;

import jipdol2.eunstargram.member.dto.request.MemberSaveRequestDTO;
import jipdol2.eunstargram.member.entity.Member;
import jipdol2.eunstargram.member.entity.MemberJpaRepository;
import jipdol2.eunstargram.member.entity.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class MemberServiceTest {

    //Service
    @Autowired
    private MemberService memberService;
    //Repository
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Test
    void clean(){
        memberJpaRepository.deleteAll();
    }

    @Test
    @DisplayName("회원 가입 테스트")
    void memberJoinTest(){

        //given
        MemberSaveRequestDTO memberSaveRequestDTO = MemberSaveRequestDTO.builder()
                .memberId("testId")
                .password("1234")
                .nickName("Rabbit")
                .phoneNumber("010-1111-2222")
                .birthDay("1994-07-15")
                .intro("안녕하세요.토끼입니다")
                .build();

        //when
        memberService.join(memberSaveRequestDTO);

        //then
        assertThat(memberJpaRepository.count()).isEqualTo(1);
        Member member = memberJpaRepository.findAll().get(0);
        assertThat(member.getMemberId()).isEqualTo("testId");
        assertThat(member.getPassword()).isEqualTo("1234");
        assertThat(member.getNickname()).isEqualTo("Rabbit");
        assertThat(member.getPhoneNumber()).isEqualTo("010-1111-2222");
        assertThat(member.getBirthDay()).isEqualTo("1994-07-15");
        assertThat(member.getIntro()).isEqualTo("안녕하세요.토끼입니다");
    }

    @Test
    @DisplayName("회원 가입 테스트 - 중복")
    void memberJoinDuplicateTest(){

        //given
        MemberSaveRequestDTO memberSaveRequestDTO1 = MemberSaveRequestDTO.builder()
                .memberId("testId")
                .password("1234")
                .nickName("Rabbit")
                .phoneNumber("010-1111-2222")
                .birthDay("1994-07-15")
                .intro("안녕하세요.토끼입니다")
                .build();

        MemberSaveRequestDTO memberSaveRequestDTO2 = MemberSaveRequestDTO.builder()
                .memberId("testId")
                .password("1234")
                .nickName("Turtle")
                .phoneNumber("010-3333-4444")
                .birthDay("1994-01-22")
                .intro("안녕하세요.거북이입니다")
                .build();
        //when
        memberService.join(memberSaveRequestDTO1);
        //then
        assertThatThrownBy(()->memberService.join(memberSaveRequestDTO2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 존재하는 회원입니다");
    }
}