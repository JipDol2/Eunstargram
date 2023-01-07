package jipdol2.eunstargram.member;

import jipdol2.eunstargram.common.EmptyJSON;
import jipdol2.eunstargram.member.dto.MemberDTO;
import jipdol2.eunstargram.member.entity.Member;
import jipdol2.eunstargram.member.entity.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public EmptyJSON join(MemberDTO memberDTO){
        validatioinDuplicateMember(memberDTO);
        memberRepository.save(Member.builder()
                .userId(memberDTO.getUserId())
                .password(memberDTO.getPassword())
                .nickname(memberDTO.getNickName())
                .phoneNumber(memberDTO.getPhoneNumber())
                .birthDay(memberDTO.getBirthDay())
                .intro(memberDTO.getIntro())
                .imagePath(memberDTO.getImagePath())
                .build());
        return new EmptyJSON();
    }

    public boolean validatioinDuplicateMember(MemberDTO memberDTO){
        return memberRepository.findByOneId(memberDTO.getUserId()).size() > 0 ? true : false;
    }

    @Transactional
    public List<Member> findByAll(){
        return memberRepository.findByAll();
    }
}
