package jipdol2.eunstargram.config;

import jipdol2.eunstargram.auth.AuthService;
import jipdol2.eunstargram.config.data.UserSession;
import jipdol2.eunstargram.exception.auth.Unauthorized;
import jipdol2.eunstargram.jwt.JwtManager;
import jipdol2.eunstargram.jwt.dto.UserSessionDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

/**
 * ArgumentResolver 는 HandlerAdapter(RequestMappingHandlerAdapter)가
 * handler(controller) 를 호출할때 파라미터,어노테이션 기반으로
 * 전달 데이터를 생성한다. *
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthResolver implements HandlerMethodArgumentResolver {

    private final AuthService authService;

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
     * Jwt Token 사용
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        return authService.findUserSessionByToken(accessToken);
    }
}
