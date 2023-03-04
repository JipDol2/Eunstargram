package jipdol2.eunstargram.auth;

import jipdol2.eunstargram.auth.dto.request.LoginRequestDTO;
import jipdol2.eunstargram.auth.entity.Session;
import jipdol2.eunstargram.exception.InvalidSigninInformation;
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

    private final MemberJpaRepository memberJpaRepository;

    @Transactional
    public String signIn(LoginRequestDTO login){
        Member findByMember = memberJpaRepository.findByMemberEmailAndPassword(login.getMemberEmail(), login.getPassword())
                .orElseThrow(InvalidSigninInformation::new);
        Session session = findByMember.addSession();
        return session.getAccessToken();
    }

}
