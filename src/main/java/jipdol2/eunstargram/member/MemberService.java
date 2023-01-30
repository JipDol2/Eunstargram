package jipdol2.eunstargram.member;

import jipdol2.eunstargram.common.dto.EmptyJSON;
import jipdol2.eunstargram.image.entity.ProfileImage;
import jipdol2.eunstargram.image.entity.ProfileImageJpaRepository;
import jipdol2.eunstargram.member.dto.request.MemberLoginRequestDTO;
import jipdol2.eunstargram.member.dto.request.MemberProfileImageDTO;
import jipdol2.eunstargram.member.dto.request.MemberSaveRequestDTO;
import jipdol2.eunstargram.member.dto.request.MemberUpdateRequestDTO;
import jipdol2.eunstargram.member.dto.response.MemberFindResponseDTO;
import jipdol2.eunstargram.member.dto.response.MemberLoginResponseDTO;
import jipdol2.eunstargram.member.entity.Member;
import jipdol2.eunstargram.member.entity.MemberJpaRepository;
import jipdol2.eunstargram.member.entity.MemberRepository;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.pool.TypePool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    //TODO: 후에 AWS(S3) 로 교체해야 함
    @Value("${com.upload.path}")
    private String uploadPath;

    private final MemberRepository memberRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final ProfileImageJpaRepository profileImageJpaRepository;

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
            throw new IllegalArgumentException("이미 존재하는 회원입니다.!!");
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

        //TODO: session 에는 어떤값을 넣으면 좋을까?

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
    public ProfileImage uploadProfileImage(MultipartFile imageDTO){
        /**
         * TODO: 이미지 파일 저장 방법?
         * https://workshop-6349.tistory.com/entry/Spring-Boot-%ED%8C%8C%EC%9D%BC%EC%9D%B4%EB%AF%B8%EC%A7%80-%EC%97%85%EB%A1%9C%EB%93%9C%ED%95%98%EA%B8%B0
         * https://velog.io/@alswl689/SpringBoot-with-JPA-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8MN-4.%EC%9D%B4%EB%AF%B8%EC%A7%80-%EC%97%85%EB%A1%9C%EB%93%9C%EC%8D%B8%EB%84%A4%EC%9D%BC%EC%9D%B4%EB%AF%B8%EC%A7%80%EC%82%AD%EC%A0%9C
         */

        //디렉토리 생성
        String toDay = String.valueOf(LocalDate.now().getYear());
        String folderPath = (uploadPath + "upload/" + toDay).replace("/",File.separator);
        File folder = new File(folderPath);

        if(!folder.exists()){
            folder.mkdirs();
        }

        String uuid = UUID.randomUUID().toString();
        String imageName = uuid + "_" + imageDTO.getOriginalFilename();
        String saveName = folderPath + File.separator + imageName;
        Path path = Paths.get(saveName);

        try{
            imageDTO.transferTo(path);
        }catch (Exception e){
            e.printStackTrace();
        }

        //TODO: ProfileImage Entity 에 save
        //TODO: 후에 memberId 를 session 에서 가져온 값으로 변경 필요
        Member findByMember = memberJpaRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("회원정보가 존재하지 않습니다."));

        ProfileImage profileImage = ProfileImage.builder()
                .originalFileName(imageDTO.getOriginalFilename())
                .storedFileName(imageName)
                .member(findByMember)
                .build();

        profileImageJpaRepository.save(profileImage);

        return profileImage;
    }
}
