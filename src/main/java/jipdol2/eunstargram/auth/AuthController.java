package jipdol2.eunstargram.auth;

import jipdol2.eunstargram.auth.dto.request.LoginRequestDTO;
import jipdol2.eunstargram.common.dto.EmptyJSON;
import jipdol2.eunstargram.config.data.UserSession;
import jipdol2.eunstargram.jwt.JwtManager;
import jipdol2.eunstargram.jwt.dto.UserSessionDTO;
import jipdol2.eunstargram.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.validation.Valid;
import java.time.Duration;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    private final JwtManager jwtManager;

//    @Value("${jwt.secret}")
//    private String KEY;

//    private static final int expireTime = 60*5*1000;   //30분

    /**
     * Q. 로그인 처리를 하고 accessToken 은 cookie 값에 담음
     *    그 후 브라우저에서 로그인을 했다는 것을 알 수 있는 수단이 있어야 됨
     *    그러나 HttpOnly 옵션을 준 상태로는 브라우저에서 cookie 값에 접근 할 수 없다
     *    그러면 어떻게 브라우저는 로그인 한 상태라는걸 알 수 있을지?
     * A. 나만의 답
     *    - 로그인 성공 후 브라우저에서 바로 로그인한 맴버의 정보를 얻는 것이 아닌
     *      한번 더 서버로 요청을 보내서 맴버의 정보를 리턴시켜주자,,,,
     * @param loginRequestDTO
     * @return
     */
    @PostMapping("/v0/login")
    public ResponseEntity<EmptyJSON> loginV0(@Valid @RequestBody LoginRequestDTO loginRequestDTO){

        log.info(">>>login={}",loginRequestDTO.toString());
        String accessToken = authService.signInSession(loginRequestDTO);

        ResponseCookie cookie = ResponseCookie.from("SESSION", accessToken)
                        .domain("localhost")    //TODO : 서버 환경에 따른 분리 필요
                        .path("/")
                        .httpOnly(true)
                        .secure(false)
                        .maxAge(Duration.ofDays(30))
                        .sameSite("Strict")
                        .build();

        log.info(">>>>>>>>>>cookie={}",cookie.toString());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE,cookie.toString())
                .header(HttpHeaders.AUTHORIZATION,accessToken)
                .body(new EmptyJSON());
    }

    @PostMapping("/v0/logout")
    public ResponseEntity<EmptyJSON> logoutV0(UserSession userSession, @CookieValue(value = "SESSION") Cookie cookie){

        String accessToken = cookie.getValue();

        authService.signOutSession(userSession.getId(),accessToken);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new EmptyJSON());
    }

    @PostMapping("/login")
    public ResponseEntity<EmptyJSON> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO){

        log.info(">>>login={}",loginRequestDTO.toString());

        Member member = authService.signInJwt(loginRequestDTO);

        UserSessionDTO sessionDTO = UserSessionDTO.builder()
                .id(member.getId())
                .email(member.getMemberEmail())
                .nickname(member.getNickname())
                .build();

        /**
         * @Value("${jwt.secret}")
         * Secret Key 생성 후 application-secret.yml 에 저장
         *
         * Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
         * byte[] encoded = key.getEncoded();
         * String secretKey = getEncoder().encodeToString(encoded);
         */

        String accessToken = jwtManager.makeToken(sessionDTO,"ACCESS");

        /**
         * accessToken 은 HEADER, refreshToken 은 Cookie 에 저장
         * - client 는 refreshToken 을 직접적으로 인증수단으로 사용하지 않음. 따라서 브라우저에서 접근하지 못하게 HttpOnly 속성 설정
         */
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION,accessToken)
                .body(new EmptyJSON());
    }
}