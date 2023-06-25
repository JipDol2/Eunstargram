package jipdol2.eunstargram.member;

import jipdol2.eunstargram.common.dto.EmptyJSON;
import jipdol2.eunstargram.crypto.MyPasswordEncoder;
import jipdol2.eunstargram.exception.image.ProfileImageNotFound;
import jipdol2.eunstargram.exception.member.MemberNotFound;
import jipdol2.eunstargram.exception.member.ValidationDuplicateMemberEmail;
import jipdol2.eunstargram.exception.member.ValidationDuplicateMemberNickname;
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
import jipdol2.eunstargram.member.entity.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    //Service
    private final ImageService imageService;
    //Respository
    private final MemberRepository memberRepository;
//    private final MemberJpaRepository memberJpaRepository;
    private final ImageJpaRepository imageJpaRepository;
    private final MyPasswordEncoder myPasswordEncoder;

    public EmptyJSON join(MemberSaveRequestDTO memberSaveRequestDTO){
        validationDuplicateMember(memberSaveRequestDTO);
        Member member = Member.transferMember(memberSaveRequestDTO);
        /** password 암호화 */
        member.encryptPassword(myPasswordEncoder);

        memberRepository.save(member);
        return new EmptyJSON();
    }

    private void validationDuplicateMember(MemberSaveRequestDTO memberSaveRequestDTO){
        if(!memberRepository.findByMemberEmail(memberSaveRequestDTO.getMemberEmail()).isEmpty()){
            throw new ValidationDuplicateMemberEmail();
        }
        if(!memberRepository.findByMemberNickname(memberSaveRequestDTO.getNickname()).isEmpty()){
            throw new ValidationDuplicateMemberNickname();
        }
    }

    @Transactional(readOnly = true)
    public MemberFindResponseDTO findByMember(Long id) {
        Member member = memberRepository.findByOne(id)
                .orElseThrow(() -> new MemberNotFound());

        MemberFindResponseDTO memberDto = MemberFindResponseDTO.createMemberFindResponseDTO(member);
        return memberDto;
    }

    @Transactional(readOnly = true)
    public MemberFindResponseDTO findByMemberEmail(String email){
        List<Member> member = memberRepository.findByMemberEmail(email);

        if(member.size() <= 0){
            throw new MemberNotFound();
        }

        MemberFindResponseDTO memberDto = MemberFindResponseDTO.createMemberFindResponseDTO(member.get(0));
        return memberDto;
    }

    @Transactional(readOnly = true)
    public MemberFindResponseDTO findByMemberNickname(String nickname){
        List<Member> member = memberRepository.findByMemberNickname(nickname);

        if(member.size() <= 0){
            throw new MemberNotFound();
        }

        MemberFindResponseDTO memberDto = MemberFindResponseDTO.createMemberFindResponseDTO(member.get(0));
        return memberDto;
    }

    public EmptyJSON update(Long seq,MemberUpdateRequestDTO memberUpdateRequestDTO){
        Member findMember = memberRepository.findByOne(seq)
                .orElseThrow(() -> new MemberNotFound());

        findMember.updateMember(memberUpdateRequestDTO);
        findMember.encryptPassword(myPasswordEncoder);
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

    @Transactional(readOnly = true)
    public List<MemberFindResponseDTO> findByAll(){
        List<Member> findMembers = memberRepository.findByAll();
        List<MemberFindResponseDTO> members = findMembers.stream()
                .map((m) -> MemberFindResponseDTO.createMemberFindResponseDTO(m))
                .collect(Collectors.toList());
        return members;
    }

    public ImageResponseDTO findByProfileImage(String nickname){

        Member findByMember = memberRepository.findByMemberNickname(nickname).get(0);

        if(findByMember == null){
            throw new ProfileImageNotFound();
        }

        return ImageResponseDTO.transfer(findByMember.getOriginalFileName(),findByMember.getStoredFileName());
    }

    public ImageResponseDTO uploadProfileImage(Long id,ImageRequestDTO imageRequestDTO){

        //기존 프로필 이미지 삭제
        removeProfileImage(id);

        MultipartFile imageFile = imageRequestDTO.getImage();

        String imageName = imageService.uploadImage(imageFile);

        Member findByMember = memberRepository.findByOne(id)
                .orElseThrow(() -> new MemberNotFound());

        findByMember.updateProfileImage(imageFile.getOriginalFilename(),imageName);

        Image image = Image.builder()
                .originalFileName(imageFile.getOriginalFilename())
                .storedFileName(imageName)
                .imageCode(ImageCode.PROFILE)
                .build();

        imageJpaRepository.save(image);

        return new ImageResponseDTO(image);
    }

    private void removeProfileImage(Long id){

        Member findByMember = memberRepository.findByOne(id)
                .orElseThrow(()->new MemberNotFound());

        Image profileImage = imageJpaRepository.findImageByStoredFileName(findByMember.getStoredFileName(), ImageCode.PROFILE)
                .orElse(null);

        if(Objects.isNull(profileImage)){
            return;
        }

        imageJpaRepository.delete(profileImage);
    }
}
