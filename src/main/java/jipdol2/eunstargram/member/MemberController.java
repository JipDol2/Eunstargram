package jipdol2.eunstargram.member;

import jipdol2.eunstargram.member.dto.MemberDTO;
import jipdol2.eunstargram.member.entity.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/signUp")
    public ResponseEntity<?> joinMember(@RequestBody MemberDTO memberDTO){
        return ResponseEntity.status(HttpStatus.OK).body(memberService.join(memberDTO));
    }
}
