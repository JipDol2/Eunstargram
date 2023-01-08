package jipdol2.eunstargram.member;

import jipdol2.eunstargram.common.EmptyJSON;
import jipdol2.eunstargram.member.dto.MemberDTO;
import jipdol2.eunstargram.member.dto.request.MemberLoginRequestDTO;
import jipdol2.eunstargram.member.entity.Member;
import jipdol2.eunstargram.member.entity.MemberJpaRepository;
import jipdol2.eunstargram.member.entity.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberJpaRepository memberJpaRepository;

    @Transactional
    public EmptyJSON join(MemberDTO memberDTO){
        validationDuplicateMember(memberDTO);
        memberRepository.save(Member.builder()
                .memberId(memberDTO.getMemberId())
                .password(memberDTO.getPassword())
                .nickname(memberDTO.getNickName())
                .phoneNumber(memberDTO.getPhoneNumber())
                .birthDay(memberDTO.getBirthDay())
                .intro(memberDTO.getIntro())
                .imagePath(memberDTO.getImagePath())
                .build());
        return new EmptyJSON();
    }

    public void validationDuplicateMember(MemberDTO memberDTO){
        if(!memberRepository.findByOneId(memberDTO.getMemberId()).isEmpty()){
            throw new IllegalArgumentException("이미 존재하는 회원입니다.!!");
        }
    }

    @Transactional
    public boolean login(MemberLoginRequestDTO memberLoginRequestDTO){
        Member member = memberJpaRepository.findByMemberIdAndPassword(memberLoginRequestDTO.getMemberId(), memberLoginRequestDTO.getPassword())
                .orElseThrow(()->new IllegalArgumentException("회원아이디 혹은 비밀번호를 잘못 입력하셨습니다."));
        /**
         * 추후에 token return 변경 필요
         */
        return true;
    }

    @Transactional
    public List<Member> findByAll(){
        return memberRepository.findByAll();
    }
}
