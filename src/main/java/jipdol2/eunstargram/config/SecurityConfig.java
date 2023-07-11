package jipdol2.eunstargram.config;

import jipdol2.eunstargram.auth.oauth.CustomOAuth2SuccessHandler;
import jipdol2.eunstargram.jwt.JwtManager;
import jipdol2.eunstargram.member.MemberController;
import jipdol2.eunstargram.member.entity.MemberJpaRepository;
import jipdol2.eunstargram.member.entity.MemberRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtManager jwtManager;
    private final MemberJpaRepository memberJpaRepository;

    public SecurityConfig(JwtManager jwtManager, MemberJpaRepository memberJpaRepository) {
        this.jwtManager = jwtManager;
        this.memberJpaRepository = memberJpaRepository;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web) -> web.ignoring().requestMatchers(
                "/js/**",
                "/img/**",
                "/css/**"
        );
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
                .csrf(csrf->csrf.disable())
                .authorizeHttpRequests(request->request
                        .anyRequest().permitAll()
                )
//                .formLogin(login->login
//                        .loginPage("/login")
//                        .usernameParameter("loginId")
//                        .passwordParameter("password")
//                        .permitAll()
//                )
                .oauth2Login(oauth->oauth
                        .successHandler(new CustomOAuth2SuccessHandler(jwtManager,memberJpaRepository))
//                        .defaultSuccessUrl("/api/auth/login")
                )
                .build();
    }
}
