package jipdol2.eunstargram.auth;

import jipdol2.eunstargram.auth.dto.request.LoginRequestDTO;
import jipdol2.eunstargram.auth.entity.Session;
import jipdol2.eunstargram.crypto.PasswordEncoder;
import jipdol2.eunstargram.exception.InvalidSignInInformation;
import jipdol2.eunstargram.exception.MemberNotFound;
import jipdol2.eunstargram.member.entity.Member;
import jipdol2.eunstargram.member.entity.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final MemberJpaRepository memberJpaRepository;

    @Transactional
    public String signInSession(LoginRequestDTO login){

        PasswordEncoder encoder = new PasswordEncoder();

        Member findByMember = memberJpaRepository.findByMemberEmailAndPassword(login.getMemberEmail(), login.getPassword())
                .orElseThrow(() -> new InvalidSignInInformation("id/password","아이디/비밀번호가 올바르지 않습니다"));
        Session session = findByMember.addSession();
        return session.getAccessToken();
    }

    @Transactional
    public Member signInJwt(LoginRequestDTO login){

        Member findByMember = memberJpaRepository.findByMemberEmail(login.getMemberEmail())
                .orElseThrow(() -> new InvalidSignInInformation("id/password","아이디/비밀번호가 올바르지 않습니다"));

        boolean matcherPassword = passwordEncoder.matcher(login.getPassword(),findByMember.getPassword());

        if(!matcherPassword){
            throw new InvalidSignInInformation("id/password","아이디/비밀번호가 올바르지 않습니다");
        }

        return findByMember;
    }
}
