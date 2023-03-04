package jipdol2.eunstargram.config;

import jipdol2.eunstargram.auth.entity.Session;
import jipdol2.eunstargram.auth.entity.SessionJpaRepository;
import jipdol2.eunstargram.config.data.UserSession;
import jipdol2.eunstargram.exception.Unauthorized;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * ArgumentResolver 는 HandlerAdapter(RequestMappingHandlerAdapter)가
 * handler(controller) 를 호출할때 파라미터,어노테이션 기반으로
 * 전달 데이터를 생성한다. *
 */
@Slf4j
@RequiredArgsConstructor
public class AuthResolver implements HandlerMethodArgumentResolver {

    private final SessionJpaRepository sessionJpaRepository;

    /**
     * parameter 에는 UserSesseion 클래스가 넘어온다.
     * 이때 supportsParameter 가 해당 클래스가 맞는지 검증한다.
     * (출처 : 김영한 mvc 1편 ArgumentResolver, ReturnValueHandler)
     * +
     * ArgumentResolver 와 ReturnValueHandler 는 HttpMessageConverter 를 이용
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserSession.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
//        String accessToken = webRequest.getHeader("Authorization");   //header 에서 token을 꺼내는 방법
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

//        if(accessToken==null || accessToken.equals("")){
//            throw new Unauthorized();
//        }

        String accessToken = cookies[0].getValue();

        Session session = sessionJpaRepository.findByAccessToken(accessToken)
                .orElseThrow(Unauthorized::new);

        return new UserSession(session.getMember().getId());
    }
}
