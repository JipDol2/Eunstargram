package jipdol2.eunstargram.member;

import jipdol2.eunstargram.common.EmptyJSON;
import jipdol2.eunstargram.member.dto.MemberDTO;
import jipdol2.eunstargram.member.dto.request.MemberLoginRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signUp")
    public ResponseEntity<EmptyJSON> joinMember(@RequestBody MemberDTO memberDTO){
        log.info("memberDTO = {}",memberDTO.toString());
        return ResponseEntity.status(HttpStatus.OK).body(memberService.join(memberDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginMember(@RequestBody MemberLoginRequestDTO memberLoginRequestDTO){
        log.info("memberLoginRequestDTO={}", memberLoginRequestDTO.toString());
        return ResponseEntity.status(HttpStatus.OK).body(memberService.login(memberLoginRequestDTO));
    }
}
