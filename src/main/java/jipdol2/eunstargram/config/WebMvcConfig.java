package jipdol2.eunstargram.config;

import jipdol2.eunstargram.auth.entity.SessionJpaRepository;
import jipdol2.eunstargram.jwt.JwtManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final JwtManager jwtManager;
    private final SessionJpaRepository sessionJpaRepository;

    /**
     * 프로젝트 내부의 이미지가 아닌 외부이미지에 접속하기 위해선 리소스 핸들러를 정의해주어야 한다.
     * - ResourceHandlerRegistry 에 대해서 반드시 공부 필요 + WebMvcConfgiruer
     * 참고
     * - https://25gstory.tistory.com/87
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:///D:/upload/2023/")
                .setCacheControl(CacheControl.maxAge(10, TimeUnit.MINUTES));    //캐싱을 10분동안 사용하겠다
    }

    /**
     * ArgumentResolver 추가
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthResolver(jwtManager));
    }

    /**
     * InterCeptor 추가
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor());
    }
}
