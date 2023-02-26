package jipdol2.eunstargram.config;

import jipdol2.eunstargram.config.data.UserSession;
import jipdol2.eunstargram.exception.Unauthorized;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * ArgumentResolver 는 HandlerAdapter(RequestMappingHandlerAdapter)가
 * handler(controller) 를 호출할때 파라미터,어노테이션 기반으로
 * 전달 데이터를 생성한다. *
 */
public class AuthResolver implements HandlerMethodArgumentResolver {

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
        String accessToken = webRequest.getParameter("accessToken");
        if(accessToken==null || accessToken.equals("")){
            throw new Unauthorized();
        }
        UserSession userSession = new UserSession();
        userSession.name=accessToken;
        return userSession;
    }
}
