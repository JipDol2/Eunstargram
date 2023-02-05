package jipdol2.eunstargram.member;

import jipdol2.eunstargram.common.dto.EmptyJSON;
import jipdol2.eunstargram.image.entity.Image;
import jipdol2.eunstargram.member.dto.request.MemberSaveRequestDTO;
import jipdol2.eunstargram.member.dto.request.MemberLoginRequestDTO;
import jipdol2.eunstargram.member.dto.request.MemberUpdateRequestDTO;
import jipdol2.eunstargram.member.dto.response.MemberFindResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    /** 2022/01/09 회원가입 API 생성 **/
    @PostMapping("/signUp")
    public ResponseEntity<EmptyJSON> joinMember(@RequestBody MemberSaveRequestDTO memberSaveRequestDTO){
        log.info("memberSaveRequestDTO = {}", memberSaveRequestDTO.toString());
        return ResponseEntity.status(HttpStatus.OK).body(memberService.join(memberSaveRequestDTO));
    }
    /** 2022/01/09 로그인 API 생성 **/
    @PostMapping("/login")
    public ResponseEntity<?> loginMember(@RequestBody MemberLoginRequestDTO memberLoginRequestDTO){
        log.info("memberLoginRequestDTO={}", memberLoginRequestDTO.toString());
        return ResponseEntity.status(HttpStatus.OK).body(memberService.login(memberLoginRequestDTO));
    }
    /** 2023/01/15 회원수정 API 생성 **/
    @PatchMapping("/update/{id}")
    public ResponseEntity<?> updateMember(@PathVariable("id") Long seq,@RequestBody MemberUpdateRequestDTO memberUpdateRequestDTO){
        log.info("memberUpdateRequestDTO={}",memberUpdateRequestDTO.toString());
        return ResponseEntity.status(HttpStatus.OK).body(memberService.update(seq,memberUpdateRequestDTO));
    }
    /** 2023/01/18 회원탈퇴 API 생성 **/
    @PatchMapping("/delete/{id}")
    public ResponseEntity<EmptyJSON> deleteMember(@PathVariable("id") Long seq){
        log.info("id={}",seq);
        return ResponseEntity.status(HttpStatus.OK).body(memberService.delete(seq));
    }
    /** 2023/01/19 회원 전체조회 API 생성 **/
    @GetMapping("/")
    public ResponseEntity<List<MemberFindResponseDTO>> findByAllMembers(){
        return ResponseEntity.status(HttpStatus.OK).body(memberService.findByAll());
    }
    /** 2023/01/19 회원 조회 API 생성 **/
    @GetMapping("/{id}")
    public ResponseEntity<MemberFindResponseDTO> findByMember(@PathVariable("id") Long seq){
        log.info("id={}",seq);
        return ResponseEntity.status(HttpStatus.OK).body(memberService.findByMember(seq));
    }
    //TODO : response 시 Image Entity 말고 DTO 를 리턴(DTO class 생성 필요)
    /** 2023/01/25 회원 프로필 이미지 업로드 API 생성 **/
    @PostMapping("/profileImage")
    public ResponseEntity<Image> uploadProfileImage(@RequestParam("image") MultipartFile profileImage){
        log.info("image={}",profileImage.getOriginalFilename());
        return ResponseEntity.status(HttpStatus.OK).body(memberService.uploadProfileImage(profileImage));
    }

}
