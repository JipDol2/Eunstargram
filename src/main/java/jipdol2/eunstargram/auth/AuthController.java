package jipdol2.eunstargram.auth;

import jipdol2.eunstargram.auth.dto.request.LoginRequestDTO;
import jipdol2.eunstargram.auth.dto.response.SessionResponseDTO;
import jipdol2.eunstargram.common.dto.EmptyJSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

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
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequestDTO loginRequestDTO){

        log.info(">>>login={}",loginRequestDTO.toString());
        String accessToken = authService.signIn(loginRequestDTO);
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
                .body(new EmptyJSON());
    }
}