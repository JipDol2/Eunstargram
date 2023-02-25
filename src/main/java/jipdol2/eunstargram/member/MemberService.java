package jipdol2.eunstargram.member;

import jipdol2.eunstargram.common.dto.EmptyJSON;
import jipdol2.eunstargram.image.ImageService;
import jipdol2.eunstargram.image.dto.ImageDTO;
import jipdol2.eunstargram.image.entity.Image;
import jipdol2.eunstargram.image.entity.ImageCode;
import jipdol2.eunstargram.image.entity.ImageJpaRepository;
import jipdol2.eunstargram.member.dto.request.MemberLoginRequestDTO;
import jipdol2.eunstargram.member.dto.request.MemberSaveRequestDTO;
import jipdol2.eunstargram.member.dto.request.MemberUpdateRequestDTO;
import jipdol2.eunstargram.member.dto.response.MemberFindResponseDTO;
import jipdol2.eunstargram.member.dto.response.MemberLoginResponseDTO;
import jipdol2.eunstargram.member.entity.Member;
import jipdol2.eunstargram.member.entity.MemberJpaRepository;
import jipdol2.eunstargram.member.entity.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    //Service
    private final ImageService imageService;
    //Respository
    private final MemberRepository memberRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final ImageJpaRepository imageJpaRepository;

    @Transactional
    public EmptyJSON join(MemberSaveRequestDTO memberSaveRequestDTO){
        validationDuplicateMember(memberSaveRequestDTO);
        memberRepository.save(Member.builder()
                .memberId(memberSaveRequestDTO.getMemberId())
                .password(memberSaveRequestDTO.getPassword())
                .nickname(memberSaveRequestDTO.getNickName())
                .phoneNumber(memberSaveRequestDTO.getPhoneNumber())
                .birthDay(memberSaveRequestDTO.getBirthDay())
                .intro(memberSaveRequestDTO.getIntro())
                .deleteYn("N")
                .build());
        return new EmptyJSON();
    }

    private void validationDuplicateMember(MemberSaveRequestDTO memberSaveRequestDTO){
        if(!memberRepository.findByMemberId(memberSaveRequestDTO.getMemberId()).isEmpty()){
            throw new IllegalArgumentException("이미 존재하는 회원입니다");
        }
    }

    @Transactional
    public MemberLoginResponseDTO login(MemberLoginRequestDTO memberLoginRequestDTO){
        Member member = memberJpaRepository.findByMemberIdAndPassword(memberLoginRequestDTO.getMemberId(), memberLoginRequestDTO.getPassword())
                .orElseThrow(()->new IllegalArgumentException("회원아이디 혹은 비밀번호를 잘못 입력하셨습니다."));

        //TODO: jwtToken 나중에 랜덤으로 token 생성된 값을 대입 수정필요
        MemberLoginResponseDTO loginResponseDTO = MemberLoginResponseDTO.builder()
                .token("fajkldjfisl")
                .id(member.getId())
                .build();


        return loginResponseDTO;
    }

    @Transactional
    public EmptyJSON update(Long seq,MemberUpdateRequestDTO memberUpdateRequestDTO){
        Member findMember = memberRepository.findByOne(seq)
                .orElseThrow(() -> new IllegalArgumentException("회원정보가 존재하지 않습니다."));

        findMember.changeMember(memberUpdateRequestDTO);
        memberRepository.save(findMember);

        return new EmptyJSON();
    }

    @Transactional
    public EmptyJSON delete(Long id) {
        Member findMember = memberRepository.findByOne(id)
                .orElseThrow(() -> new IllegalArgumentException("회원정보가 존재하지 않습니다."));

        findMember.changeDeleteYn("N");
        memberRepository.save(findMember);

        return new EmptyJSON();
    }

    @Transactional
    public List<MemberFindResponseDTO> findByAll(){
        List<Member> findMembers = memberRepository.findByAll();
        List<MemberFindResponseDTO> members = findMembers.stream()
                .map((m) -> MemberFindResponseDTO.createMemberFindResponseDTO(m))
                .collect(Collectors.toList());
        return members;
    }

    @Transactional
    public MemberFindResponseDTO findByMember(Long seq) {
        Member member = memberRepository.findByOne(seq)
                .orElseThrow(() -> new IllegalArgumentException("회원정보가 존재하지 않습니다."));

        MemberFindResponseDTO memberFindResponseDTO = MemberFindResponseDTO.createMemberFindResponseDTO(member);
        return memberFindResponseDTO;
    }

    @Transactional
    public ImageDTO uploadProfileImage(MultipartFile imageDTO){

        String imageName = imageService.uploadImage(imageDTO);

        //TODO: Image Entity 에 save
        //TODO: 후에 memberId 를 session 에서 가져온 값으로 변경 필요
        Member findByMember = memberJpaRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("회원정보가 존재하지 않습니다."));

        Image image = Image.builder()
                .originalFileName(imageDTO.getOriginalFilename())
                .storedFileName(imageName)
                .member(findByMember)
                .imageCode(ImageCode.PROFILE)
                .build();

        imageJpaRepository.save(image);

        return new ImageDTO(image);
    }


}
