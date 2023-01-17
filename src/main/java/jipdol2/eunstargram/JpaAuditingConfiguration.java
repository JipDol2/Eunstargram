package jipdol2.eunstargram;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * JpaAuditingConfiguration 를 따로 생성한 이유
 * https://sup2is.tistory.com/107
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfiguration {
}
