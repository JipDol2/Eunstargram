package jipdol2.eunstargram.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jipdol2.eunstargram.auth.dto.request.LoginRequestDTO;
import jipdol2.eunstargram.auth.dto.response.LoginCheckDTO;
import jipdol2.eunstargram.auth.entity.NoAuth;
import jipdol2.eunstargram.common.dto.EmptyJSON;
import jipdol2.eunstargram.config.data.UserSession;
import jipdol2.eunstargram.exception.auth.MissAuthorized;
import jipdol2.eunstargram.exception.auth.Unauthorized;
import jipdol2.eunstargram.jwt.dto.TokenResponse;
import jipdol2.eunstargram.member.MemberService;
import jipdol2.eunstargram.member.dto.response.MemberFindResponseDTO;
import jipdol2.eunstargram.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    private final MemberService memberService;

//    private final JwtManager jwtManager;

    private static final String ACCESS_TOKEN = "ACCESS";

    private static final String REFRESH_TOKEN = "REFRESH";

//    private static final int expireTime = 60*5*1000;   //30분

    /**
     * Q. 로그인 처리를 하고 accessToken 은 cookie 값에 담음
     * 그 후 브라우저에서 로그인을 했다는 것을 알 수 있는 수단이 있어야 됨
     * 그러나 HttpOnly 옵션을 준 상태로는 브라우저에서 cookie 값에 접근 할 수 없다
     * 그러면 어떻게 브라우저는 로그인 한 상태라는걸 알 수 있을지?
     * A. 나만의 답
     * - 로그인 성공 후 브라우저에서 바로 로그인한 맴버의 정보를 얻는 것이 아닌
     * 한번 더 서버로 요청을 보내서 맴버의 정보를 리턴시켜주자,,,,
     *
     * @param loginRequestDTO
     * @return
     */
/*    @PostMapping("/v0/login")
    public ResponseEntity<EmptyJSON> loginV0(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {

        log.info(">>>login={}", loginRequestDTO.toString());
        String accessToken = authService.signInSession(loginRequestDTO);

        ResponseCookie cookie = ResponseCookie.from("SESSION", ACCESS_TOKEN)
                .domain("localhost")    //TODO : 서버 환경에 따른 분리 필요
                .path("/")
                .httpOnly(true)
                .secure(false)
                .maxAge(Duration.ofDays(30))
                .sameSite("Strict")
                .build();

        log.info(">>>>>>>>>>cookie={}", cookie.toString());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .body(new EmptyJSON());
    }*/

/*    @PostMapping("/v0/logout")
    public ResponseEntity<EmptyJSON> logoutV0(UserSession userSession, @CookieValue(value = "SESSION") Cookie cookie) {

        String accessToken = cookie.getValue();

        authService.signOutSession(userSession.getId(), accessToken);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new EmptyJSON());
    }*/

    @NoAuth
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO, HttpServletRequest request) {

        log.info(">>>login={}", loginRequestDTO.toString());

        Member member = authService.signInJwt(loginRequestDTO);
        String accessToken = authService.createAccessToken(member.getId());
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
        String refreshToken = cookies[0].getValue();

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
}