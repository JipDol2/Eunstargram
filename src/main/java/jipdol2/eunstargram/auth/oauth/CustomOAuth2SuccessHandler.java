package jipdol2.eunstargram.auth.oauth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jipdol2.eunstargram.jwt.JwtManager;
import jipdol2.eunstargram.member.entity.Member;
import jipdol2.eunstargram.member.entity.MemberJpaRepository;
import jipdol2.eunstargram.member.entity.SocialMember;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtManager jwtManager;
    private final MemberJpaRepository memberJpaRepository;

    public CustomOAuth2SuccessHandler(JwtManager jwtManager, MemberJpaRepository memberJpaRepository) {
        this.jwtManager = jwtManager;
        this.memberJpaRepository = memberJpaRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User user = (OAuth2User) authentication.getPrincipal();
        OAuth2AuthenticationToken authenticationToken = (OAuth2AuthenticationToken) authentication;

        Map<String, Object> attributes = user.getAttributes();
        Long id = Long.valueOf((Integer)attributes.get("id"));
        String registerId = authenticationToken.getAuthorizedClientRegistrationId();

        Optional<SocialMember> findMember = memberJpaRepository.findBySocialId(id);

        if(findMember.isEmpty()){
            //TODO : cookie 에 저장하는 건 어떨까?
            this.getRedirectStrategy().sendRedirect(request,response,"/signUp-social");
            return;
        }

        Member member = findMember.stream()
                .findFirst()
                .get();
        String accessToken = jwtManager.makeAccessToken(member.getId());

        String url = UriComponentsBuilder.fromUriString("/redirect/login")
                .queryParam("accessToken",accessToken)
                .build()
                .toString();
        this.getRedirectStrategy().sendRedirect(request,response,url);
    }
}
