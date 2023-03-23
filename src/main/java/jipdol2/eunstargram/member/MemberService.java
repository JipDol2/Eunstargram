package jipdol2.eunstargram.member;

import jipdol2.eunstargram.common.dto.EmptyJSON;
import jipdol2.eunstargram.exception.MemberNotFound;
import jipdol2.eunstargram.image.ImageService;
import jipdol2.eunstargram.image.dto.request.ImageRequestDTO;
import jipdol2.eunstargram.image.dto.response.ImageResponseDTO;
import jipdol2.eunstargram.image.entity.Image;
import jipdol2.eunstargram.image.entity.ImageCode;
import jipdol2.eunstargram.image.entity.ImageJpaRepository;
import jipdol2.eunstargram.member.dto.request.MemberSaveRequestDTO;
import jipdol2.eunstargram.member.dto.request.MemberUpdateRequestDTO;
import jipdol2.eunstargram.member.dto.response.MemberFindResponseDTO;
import jipdol2.eunstargram.member.entity.Member;
import jipdol2.eunstargram.member.entity.MemberJpaRepository;
import jipdol2.eunstargram.member.entity.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    //Service
    private final ImageService imageService;
    //Respository
    private final MemberRepository memberRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final ImageJpaRepository imageJpaRepository;

    public EmptyJSON join(MemberSaveRequestDTO memberSaveRequestDTO){
        validationDuplicateMember(memberSaveRequestDTO);

        SCryptPasswordEncoder encoder =
                new SCryptPasswordEncoder(32,8,1,32,64);

        String cryptPassword = encoder.encode(memberSaveRequestDTO.getPassword());

        memberRepository.save(Member.builder()
                .memberEmail(memberSaveRequestDTO.getMemberEmail())
                .password(cryptPassword)
                .nickname(memberSaveRequestDTO.getNickname())
                .phoneNumber(memberSaveRequestDTO.getPhoneNumber())
                .birthDay(memberSaveRequestDTO.getBirthDay())
                .intro(memberSaveRequestDTO.getIntro())
                .build());
        return new EmptyJSON();
    }

    //TODO: 중복 회원 exception 생성필요
    private void validationDuplicateMember(MemberSaveRequestDTO memberSaveRequestDTO){
        if(!memberRepository.findByMemberId(memberSaveRequestDTO.getMemberEmail()).isEmpty()){
            throw new IllegalArgumentException("이미 존재하는 회원입니다");
        }
    }

    public MemberFindResponseDTO findByMember(Long id) {
        Member member = memberRepository.findByOne(id)
                .orElseThrow(() -> new MemberNotFound());

        MemberFindResponseDTO memberFindResponseDTO = MemberFindResponseDTO.createMemberFindResponseDTO(member);
        return memberFindResponseDTO;
    }

    public EmptyJSON update(Long seq,MemberUpdateRequestDTO memberUpdateRequestDTO){
        Member findMember = memberRepository.findByOne(seq)
                .orElseThrow(() -> new MemberNotFound());

        findMember.changeMember(memberUpdateRequestDTO);
        /**
         * save 를 할 필요가 없다. dirty checking 이 일어나기 때문
         */
//        memberRepository.save(findMember);
        return new EmptyJSON();
    }

    public EmptyJSON delete(Long id) {
        Member findMember = memberRepository.findByOne(id)
                .orElseThrow(() -> new MemberNotFound());

        findMember.changeDeleteYn("N");
        memberRepository.save(findMember);

        return new EmptyJSON();
    }

    public List<MemberFindResponseDTO> findByAll(){
        List<Member> findMembers = memberRepository.findByAll();
        List<MemberFindResponseDTO> members = findMembers.stream()
                .map((m) -> MemberFindResponseDTO.createMemberFindResponseDTO(m))
                .collect(Collectors.toList());
        return members;
    }

    public ImageResponseDTO uploadProfileImage(ImageRequestDTO imageRequestDTO){

        MultipartFile imageFile = imageRequestDTO.getImage();

        String imageName = imageService.uploadImage(imageFile);

        //TODO: Image Entity 에 save
        //TODO: 후에 memberId 를 session 에서 가져온 값으로 변경 필요
        Member findByMember = memberJpaRepository.findById(imageRequestDTO.getMemberId())
                .orElseThrow(() -> new MemberNotFound());

        Image image = Image.builder()
                .originalFileName(imageFile.getOriginalFilename())
                .storedFileName(imageName)
                .member(findByMember)
                .imageCode(ImageCode.PROFILE)
                .build();

        imageJpaRepository.save(image);

        return new ImageResponseDTO(image);
    }
}
