package jipdol2.eunstargram.config;

import jipdol2.eunstargram.auth.AuthService;
import jipdol2.eunstargram.auth.entity.NoAuth;
import jipdol2.eunstargram.exception.auth.Unauthorized;
import jipdol2.eunstargram.exception.auth.AccessTokenRenew;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(handler instanceof HandlerMethod){
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            NoAuth noAuth = handlerMethod.getMethodAnnotation(NoAuth.class);
            if(noAuth!=null){
                return true;
            }
            noAuth = handlerMethod.getBeanType().getAnnotation(NoAuth.class);
            if(noAuth!=null){
                return true;
            }
        }

        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(isValidAccessToken(accessToken)){
            return true;
        }

        String refreshToken = extractRefreshToken(request);
        authService.validateRefreshToken(refreshToken);
        throw new AccessTokenRenew();
    }

    public boolean isValidAccessToken(String accessToken){
        authService.validateAccessToken(accessToken);
        return true;
    }

    public String extractRefreshToken(HttpServletRequest request){
        Cookie cookie = WebUtils.getCookie(request,"REFRESH_TOKEN");
        if(cookie == null){
            throw new Unauthorized();
        }
        return cookie.getValue();
    }
}
