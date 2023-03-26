package jipdol2.eunstargram.config;

import jipdol2.eunstargram.auth.entity.SessionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final SessionJpaRepository sessionJpaRepository;

    private final AuthResolver authResolver;

    /**
     * 프로젝트 내부의 이미지가 아닌 외부이미지에 접속하기 위해선 리소스 핸들러를 정의해주어야 한다.
     * - ResourceHandlerRegistry 에 대해서 반드시 공부 필요 + WebMvcConfgiruer
     * 참고
     * - https://25gstory.tistory.com/87
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:///D:/upload/2023/");
    }

    /**
     * ArgumentResolver 추가
     * - AuthResolver
     * @param resolvers initially an empty list
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
//        resolvers.add(new AuthResolver(sessionJpaRepository));
        resolvers.add(authResolver);
    }
}
