package jipdol2.eunstargram.member;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jipdol2.eunstargram.auth.entity.NoAuth;
import jipdol2.eunstargram.common.dto.EmptyJSON;
import jipdol2.eunstargram.config.data.UserSession;
import jipdol2.eunstargram.image.dto.request.ImageRequestDTO;
import jipdol2.eunstargram.image.dto.response.ImageResponseDTO;
import jipdol2.eunstargram.member.dto.request.MemberEmailRequestDTO;
import jipdol2.eunstargram.member.dto.request.MemberSaveRequestDTO;
import jipdol2.eunstargram.member.dto.request.MemberUpdateRequestDTO;
import jipdol2.eunstargram.member.dto.response.MemberFindResponseDTO;
import jipdol2.eunstargram.member.dto.response.MemberValidationCheckEmailDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    /** 2022/01/09 회원가입 API 생성 **/
    @NoAuth
    @PostMapping("/signUp")
    public ResponseEntity<EmptyJSON> joinMember(@Valid @RequestBody MemberSaveRequestDTO memberSaveRequestDTO){
        log.info("memberSaveRequestDTO = {}", memberSaveRequestDTO.toString());
        return ResponseEntity.ok().body(memberService.join(memberSaveRequestDTO));
    }
    /** 2023/01/19 회원 전체조회 API 생성 **/
    @GetMapping("/")
    public ResponseEntity<List<MemberFindResponseDTO>> findByAllMembers(){
        return ResponseEntity.ok().body(memberService.findByAll());
    }

    /** 2023/01/19 회원 조회 API 생성 **/
    @GetMapping("/{id}")
    public ResponseEntity<MemberFindResponseDTO> findByMember(@PathVariable("id") Long seq){
        log.info("id={}",seq);
        return ResponseEntity.ok().body(memberService.findByMember(seq));
    }

    /** 2023/03/05 회원정보 조회 **/
    @GetMapping("/me")
    public ResponseEntity<MemberFindResponseDTO> findByMyInfo(UserSession userSession){
        log.info("id={}",userSession.getId());
        return ResponseEntity.ok().body(memberService.findByMember(userSession.getId()));
    }

    /** 2023/04/22 닉네임으로 회원정보 조회 **/
    @GetMapping("/findByMember/{nickname}")
    public ResponseEntity<MemberFindResponseDTO> findByMember(@PathVariable("nickname") String nickname){
        log.info("nickname={}",nickname);
        return ResponseEntity.ok().body(memberService.findByMemberNickname(nickname));
    }

    /** 2023/01/15 회원정보수정 API 생성 **/
    @PatchMapping("/update/{id}")
    public ResponseEntity<?> updateMember(@PathVariable("id") Long seq,@RequestBody MemberUpdateRequestDTO memberUpdateRequestDTO){
        log.info("memberUpdateRequestDTO={}",memberUpdateRequestDTO.toString());
        return ResponseEntity.ok().body(memberService.update(seq,memberUpdateRequestDTO));
    }

    /** 2023/01/18 회원탈퇴 API 생성 **/
    @PatchMapping("/delete/{id}")
    public ResponseEntity<EmptyJSON> deleteMember(@PathVariable("id") Long seq){
        log.info("id={}",seq);
        return ResponseEntity.ok().body(memberService.delete(seq));
    }

    /** 2023/04/09 회원 프로필 이미지 조회 API 생성**/
    @GetMapping("/profileImage/{nickname}")
    public ResponseEntity<ImageResponseDTO> findByProfileImage(@PathVariable("nickname") String nickname){
        return ResponseEntity.ok().body(memberService.findByProfileImage(nickname));
    }

    /** 2023/01/25 회원 프로필 이미지 업로드 API 생성 **/
    @PostMapping("/profileImage")
    public ResponseEntity<ImageResponseDTO> uploadProfileImage(UserSession userSession,@ModelAttribute ImageRequestDTO imageRequestDTO){
        log.info("image={}",imageRequestDTO.getImage().getOriginalFilename());
        return ResponseEntity.ok().body(memberService.uploadProfileImage(userSession.getId(),imageRequestDTO));
    }

    @NoAuth
    @PostMapping("/validation/email")
    public ResponseEntity<MemberValidationCheckEmailDTO> validationCheckEmail(@RequestBody MemberEmailRequestDTO emailRequestDTO){
        log.info("memberEmail={}",emailRequestDTO.toString());
        return ResponseEntity.ok().body(memberService.validationCheckEmail(emailRequestDTO.getEmail()));
    }

    @NoAuth
    @PostMapping("/email/social")
    public ResponseEntity<Object> emailConnectionToSocial(@RequestBody MemberEmailRequestDTO emailRequestDTO, HttpSession session){
        memberService.connectToSocial(emailRequestDTO.getEmail(),
                (int) session.getAttribute("socialId"),
                (String) session.getAttribute("socialProvider"));
        return ResponseEntity.ok().body(new EmptyJSON());
    }
}