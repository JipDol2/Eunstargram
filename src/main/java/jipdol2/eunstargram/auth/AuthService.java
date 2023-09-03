package jipdol2.eunstargram.auth;

import jipdol2.eunstargram.auth.dto.request.LoginRequestDTO;
import jipdol2.eunstargram.auth.entity.RefreshToken;
import jipdol2.eunstargram.auth.entity.RefreshTokenRepository;
import jipdol2.eunstargram.config.data.UserSession;
import jipdol2.eunstargram.crypto.MyPasswordEncoder;
import jipdol2.eunstargram.exception.member.InvalidSignInInformation;
import jipdol2.eunstargram.jwt.JwtManager;
import jipdol2.eunstargram.member.entity.Member;
import jipdol2.eunstargram.member.entity.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberJpaRepository memberJpaRepository;

    private final MyPasswordEncoder myPasswordEncoder;
    private final JwtManager jwtManager;

    public Member signInJwt(LoginRequestDTO login) {

        Member findByMember = memberJpaRepository.findByMemberEmail(login.getMemberEmail())
                .orElseThrow(() -> new InvalidSignInInformation("id/password", "아이디/비밀번호가 올바르지 않습니다"));

        boolean matcherPassword = myPasswordEncoder.matcher(login.getPassword(), findByMember.getPassword());

        if (!matcherPassword) {
            throw new InvalidSignInInformation("id/password", "아이디/비밀번호가 올바르지 않습니다");
        }

        return findByMember;
    }

    public String createAccessToken(LoginRequestDTO dto){
        //일반 로그인
        Member member = signInJwt(dto);
        Long id = member.getId();
        return jwtManager.makeAccessToken(id);
    }

    public String createRefreshToken(Long id){
        String refreshToken = jwtManager.makeRefreshToken(id);
        refreshTokenRepository.save(new RefreshToken(refreshToken));
        return refreshToken;
    }

    public void removeRefreshToken(String refreshToken){
        RefreshToken findByRefreshToken = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException());
        refreshTokenRepository.deleteById(findByRefreshToken.getId());
    }

    public void validateAccessToken(String accessToken){
        jwtManager.validateToken(accessToken);
    }

    public void validateRefreshToken(String refreshToken){
        jwtManager.validateToken(refreshToken);
    }

    public Long extractMemberIdFromToken(String accessToken){
        return jwtManager.getMemberIdFromToken(accessToken);
    }

    public UserSession findUserSessionByToken(String accessToken){
        Long id = extractMemberIdFromToken(accessToken);
        return UserSession.builder()
                .id(id)
                .build();
    }
}