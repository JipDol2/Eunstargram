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
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.util.Arrays;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    private final MemberService memberService;



    @NoAuth
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(
//            @PathVariable String provider,
            @Valid @RequestBody LoginRequestDTO loginRequestDTO
    ) {
//        log.info(">>>provider={}",provider);
        log.info(">>>login={}", loginRequestDTO.toString());

//        Member member = authService.signInJwt(loginRequestDTO);
        String accessToken = authService.createAccessToken(loginRequestDTO);
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

    @NoAuth
    @GetMapping("/login/oauth2/callback")
    public void loginCallbackSocial(
            @RequestParam String code,
//            @PathVariable String provider,
            HttpServletResponse response){

        UriComponents uri = UriComponentsBuilder
                .fromUriString("http://localhost:8080")
                .path("/oauthCallback")
                .queryParam("code", code)
//                .queryParam("provider", provider)
                .build();

        try{
            response.sendRedirect(uri.toString());
        }catch(IOException e){
            throw new IllegalArgumentException("Redirection Error",e);
        }
    }
}