package jipdol2.eunstargram.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jipdol2.eunstargram.auth.dto.request.LoginRequestDTO;
import jipdol2.eunstargram.auth.dto.response.LoginCheckDTO;
import jipdol2.eunstargram.auth.entity.NoAuth;
import jipdol2.eunstargram.common.dto.EmptyJSON;
import jipdol2.eunstargram.config.data.UserSession;
import jipdol2.eunstargram.exception.auth.MissAuthorized;
import jipdol2.eunstargram.exception.auth.Unauthorized;
import jipdol2.eunstargram.jwt.JwtManager;
import jipdol2.eunstargram.jwt.dto.TokenResponse;
import jipdol2.eunstargram.member.MemberService;
import jipdol2.eunstargram.member.dto.response.MemberFindResponseDTO;
import jipdol2.eunstargram.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    private final MemberService memberService;

    /**
     * 1. provider 로 해당 소셜플랫폼 type 을 가져온다.
     * 2. 해당 type 으로 OAuthManager(GithubManager,GoogleManger 등) 를 구한다.
     * 3. OAuthManager 를 통해 code 와 함께 accessToken 을 요청하고 accessToken 을 통해 oauth user 정보를 가져온다.
     * 4. Github 와 Google 이냐에 따라서 달리 처리되어야 한다. (Github 는 email 정보를 return 하지 않는다)
     *      4-1. Github 일때
     *          1) return 받은 id 로 회원여부를 체킹
     *          2) 회원이 아니라면 이메일을 입력받아야 하고 해당 이메일로 회원가입 절차가 진행되어야 한다.
     *              2-1) 입력한 이메일이 이미 회원가입이 완료된 이메일이라면 '이미 가입된 이메일' 이라고 알려주고,
     *                  해당 계정으로 Github 를 연동해야된다.
     *              2-2) 입력한 이메일로 가입된 계정이 없다면 회원가입 절차를 진행한다.
     *          3) 회원이라면 로그인 절차를 진행한다.
     *      4-2. Github 가 아닐때
     *          1) return 받은 email 로 회원여부를 체킹
     *          2) 회원이 아니라면 회원가입 동의 후 가입 진행
     *          3) 회원이라면 로그인 절차 진행한다.
     * 5. Client(Brower) 에게 전달할 access_token 생성 후 전송
     */

    @NoAuth
    @PostMapping("/login/{provider}")
    public ResponseEntity<TokenResponse> login(
            @PathVariable String provider,
            @Valid @RequestBody LoginRequestDTO loginRequestDTO
    ) {
        log.info(">>>provider={}",provider);
        log.info(">>>login={}", loginRequestDTO.toString());

        Member member = authService.signInJwt(loginRequestDTO);
        String accessToken = authService.createAccessToken(provider,member.getId());
        Long id = authService.extractMemberIdFromToken(accessToken);
        ResponseCookie cookie = createRefreshTokenCookie(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(TokenResponse.of(accessToken));
    }

    private ResponseCookie createRefreshTokenCookie(Long id){
        String refreshToken = authService.createRefreshToken(id);
        return ResponseCookie.from("REFRESH_TOKEN", refreshToken)
                .domain("localhost")    //TODO : 서버 환경에 따른 분리 필요
                .path("/")
                .httpOnly(true)
                .secure(false)
                .maxAge(Duration.ofDays(30))
                .sameSite("Strict")
                .build();
    }

    @NoAuth
    @PostMapping("/logout")
    public ResponseEntity<EmptyJSON> logout(HttpServletRequest request) {

        if (request == null) {
            throw new Unauthorized();
        }
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new Unauthorized();
        }

        String refreshToken = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("REFRESH_TOKEN"))
                .findFirst()
                .map(cookie -> cookie.getValue())
                .orElse(null);

        ResponseCookie cookie = expiredRefreshToken(refreshToken);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new EmptyJSON());
    }

    public ResponseCookie expiredRefreshToken(String refreshToken){
        //refreshToken 제거
        authService.removeRefreshToken(refreshToken);
        //cookie 의 종료 날짜를 0으로 설정
        return ResponseCookie.from("REFRESH_TOKEN", "")
                .domain("localhost")    //TODO : 서버 환경에 따른 분리 필요
                .path("/")
                .httpOnly(true)
                .secure(false)
                .maxAge(0)
                .sameSite("Strict")
                .build();
    }

    @NoAuth
    @GetMapping("/loginCheck")
    public ResponseEntity<LoginCheckDTO> loginCheck(HttpServletRequest request){

        String accessToken = request.getHeader("Authorization");
        if(accessToken.equals("null")){
            return ResponseEntity.ok()
                    .body(LoginCheckDTO.of(false));
        }
        authService.validateAccessToken(accessToken);
        return ResponseEntity.ok()
                .body(LoginCheckDTO.of(true));
    }

    @NoAuth
    @GetMapping("/checkAuth")
    public ResponseEntity<EmptyJSON> checkAuth(UserSession userSession,@RequestParam("nickname") String nickname){

        MemberFindResponseDTO member = memberService.findByMember(userSession.getId());

        if(!member.getNickname().equals(nickname)){
            log.info("권한이 존재하지 않습니다.");
            throw new MissAuthorized();
        }
        return ResponseEntity.ok()
                .body(new EmptyJSON());
    }

    //    RedirectAttirbutes
    @NoAuth
    @GetMapping("/login/callback/{provider}")
    public void loginCallbackSocial(
            @RequestParam String code,
            @PathVariable String provider,
            RedirectAttributes redirectAttributes, HttpServletResponse response){

        UriComponents uri = UriComponentsBuilder.newInstance()
                .path("/oauthCallback")
//                .queryParam("code", code)
                .queryParam("provider", provider)
                .build();

        redirectAttributes.addFlashAttribute("code",code);
//        redirectAttributes.addFlashAttribute("provider",provider);
        try{
            response.sendRedirect(uri.toString());
        }catch(IOException e){
            throw new IllegalArgumentException("Redirection Error",e);
        }
    }
}