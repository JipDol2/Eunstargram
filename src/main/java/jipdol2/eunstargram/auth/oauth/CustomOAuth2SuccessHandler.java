//package jipdol2.eunstargram.auth.oauth;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//import jipdol2.eunstargram.auth.AuthService;
//import jipdol2.eunstargram.exception.member.MemberNotFound;
//import jipdol2.eunstargram.member.entity.Member;
//import jipdol2.eunstargram.member.entity.MemberJpaRepository;
//import jipdol2.eunstargram.member.entity.SocialProvider;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.ResponseCookie;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
//import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
//import org.springframework.web.util.UriComponentsBuilder;
//
//import java.io.IOException;
//import java.time.Duration;
//import java.util.Map;
//import java.util.Optional;
//
//public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
//
//    private final AuthService authService;
//    private final MemberJpaRepository memberJpaRepository;
//
//    public CustomOAuth2SuccessHandler(AuthService authService, MemberJpaRepository memberJpaRepository) {
//        this.authService = authService;
//        this.memberJpaRepository = memberJpaRepository;
//    }
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//        OAuth2AuthenticationToken authenticationToken = (OAuth2AuthenticationToken) authentication;
//
//        //OAuth 속성값들을 추출한다
//        Map<String, Object> attributes = authenticationToken.getPrincipal().getAttributes();
//        Long id = Long.valueOf((Integer)attributes.get("id"));
//        String registerId = authenticationToken.getAuthorizedClientRegistrationId();
//
//        //이미 회원가입이 되어있는지 확인
//        Optional<Member> findMember = memberJpaRepository.findBySocialId(id);
//
//        //회원가입이 되어 있지 않은 상태라면 github 소셜과 연동하기 위해 이메일을 입력하는 절차가 필요하다
//        //이메일을 입력한 뒤에 OAuth 정보들을 함께 알아야지만 연동이 가능한데 이를 어떻게 구현해 줄 수 있을까?
//        //=>쿠키에 저장해서 전송하는 방법이 있을 수 있지 않을까?
//        //->session 에 저장하는 방식을 사용
//        if(findMember.isEmpty()){
//            if(SocialProvider.from(registerId).equals(SocialProvider.GITHUB)){
//                HttpSession session = request.getSession();
//                session.setAttribute("socialId",attributes.get("id"));
//                session.setAttribute("socialProvider","GITHUB");
//                this.getRedirectStrategy().sendRedirect(request,response,"/signUp-social");
//            }
//            return;
//        }
//
//        //회원가입이 이미 되어 있는 상태라면 accessToken 생성 후 client 에게 전송
//        Member member = findMember.orElseThrow(()->new MemberNotFound());
//        String accessToken = authService.createAccessToken(member.getId());
//
//        //refreshToken cookie 저장
//        String refreshToken = authService.createRefreshToken(member.getId());
//        response.setHeader(HttpHeaders.SET_COOKIE,makeCookie(refreshToken).toString());
//
//        String url = UriComponentsBuilder.fromUriString("/redirect/login")
//                .queryParam("accessToken",accessToken)
//                .build()
//                .toString();
//        this.getRedirectStrategy().sendRedirect(request,response,url);
//    }
//
//    private ResponseCookie makeCookie(String refreshToken){
//        return ResponseCookie.from("REFRESH_TOKEN", refreshToken)
//                .domain("localhost")
//                .path("/")
//                .httpOnly(true)
//                .secure(false)
//                .maxAge(Duration.ofDays(30))
//                .sameSite("Strict")
//                .build();
//    }
//}
