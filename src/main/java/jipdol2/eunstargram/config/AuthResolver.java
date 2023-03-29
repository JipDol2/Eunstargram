package jipdol2.eunstargram.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jipdol2.eunstargram.auth.entity.Session;
import jipdol2.eunstargram.auth.entity.SessionJpaRepository;
import jipdol2.eunstargram.config.data.UserSession;
import jipdol2.eunstargram.exception.Unauthorized;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Base64;

/**
 * ArgumentResolver 는 HandlerAdapter(RequestMappingHandlerAdapter)가
 * handler(controller) 를 호출할때 파라미터,어노테이션 기반으로
 * 전달 데이터를 생성한다. *
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthResolver implements HandlerMethodArgumentResolver {

    private final SessionJpaRepository sessionJpaRepository;

    @Value("${jwt.secret}")
    private String KEY;

    /**
     * parameter 에는 UserSesseion 클래스가 넘어온다.
     * 이때 supportsParameter 가 해당 클래스가 맞는지 검증한다.
     * (출처 : 김영한 mvc 1편 ArgumentResolver, ReturnValueHandler)
     * +
     * ArgumentResolver 와 ReturnValueHandler 는 HttpMessageConverter 를 이용
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        /**
         * API 의 파라미터로 UserSession class 를 받고 있는지 체크
         * 만약 파라미터로 UserSession class 를 받고 있으면 ArgumentResolver 의 체크 대상이 된다
         */
        return parameter.getParameterType().equals(UserSession.class);
    }

    /**
     * UUID Token 값을 DB 에 저장 후에 조회한 후 인증 절차 진행
     */
/*    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if(request == null){
            log.error("servletRequest null");
            throw new Unauthorized();
        }
        Cookie[] cookies = request.getCookies();
        if(cookies == null){
            log.error("cookie is null");
            throw new Unauthorized();
        }

        String accessToken = cookies[0].getValue();

        Session session = sessionJpaRepository.findByAccessToken(accessToken)
                .orElseThrow(Unauthorized::new);

        return UserSession.builder()
                .id(session.getMember().getId())
                .email(session.getMember().getMemberEmail())
                .nickname(session.getMember().getNickname())
                .build();
    }*/

    /**
     * Jwt Token 사용
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String jws = webRequest.getHeader("Authorization");   //header 에서 token을 꺼내는 방법

        if(jws == null || jws.equals("")){
            log.error("servletRequest null");
            throw new Unauthorized();
        }
        byte[] decodedKey = Base64.getDecoder().decode(KEY);
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(decodedKey)
                    .build()
                    .parseClaimsJws(jws);

            Long userId = claims.getBody().get("id",Long.class);
            String email = claims.getBody().get("email",String.class);
            String nickname = claims.getBody().get("nickname",String.class);

            //TODO: redis 에서 userId 를 key 값으로 조회 체크
            //TODO: 만약 accessToken 이 시간이 만료되고 refreshToken 이 존재한다면 재발급
            //TODO: 그렇지 않고 refreshToken 이 존재하지 않는다면 로그인 상태가 아니라는 뜻이므로 exception 발생

            return UserSession.builder()
                    .id(userId)
                    .email(email)
                    .nickname(nickname)
                    .build();
        } catch (JwtException e) {
            throw new Unauthorized();
        }
    }
}
