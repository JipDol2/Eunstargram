package jipdol2.eunstargram.member;

import jipdol2.eunstargram.common.dto.EmptyJSON;
import jipdol2.eunstargram.member.dto.request.MemberSaveRequestDTO;
import jipdol2.eunstargram.member.dto.request.MemberLoginRequestDTO;
import jipdol2.eunstargram.member.dto.request.MemberUpdateRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    //TODO: 2022/01/09 회원가입 API 생성
    @PostMapping("/signUp")
    public ResponseEntity<EmptyJSON> joinMember(@RequestBody MemberSaveRequestDTO memberSaveRequestDTO){
        log.info("memberSaveRequestDTO = {}", memberSaveRequestDTO.toString());
        return ResponseEntity.status(HttpStatus.OK).body(memberService.join(memberSaveRequestDTO));
    }
    //TODO: 2022/01/09 로그인 API 생성
    @PostMapping("/login")
    public ResponseEntity<?> loginMember(@RequestBody MemberLoginRequestDTO memberLoginRequestDTO){
        log.info("memberLoginRequestDTO={}", memberLoginRequestDTO.toString());
        return ResponseEntity.status(HttpStatus.OK).body(memberService.login(memberLoginRequestDTO));
    }
    //TODO: 2023/01/15 회원수정 API 생성
    @PatchMapping("/update/{id}")
    public ResponseEntity<?> updateMember(@PathVariable("id") Long seq,@RequestBody MemberUpdateRequestDTO memberUpdateRequestDTO){
        log.info("memberUpdateRequestDTO={}",memberUpdateRequestDTO.toString());
        return ResponseEntity.status(HttpStatus.OK).body(seq,memberUpdateRequestDTO);
    }
}
