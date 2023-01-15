package jipdol2.eunstargram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @EnableJpaAuditing : JPA Auditing 기능을 활성화 시켜주기 위해 적용
 */
@EnableJpaAuditing
@SpringBootApplication
public class EunstargramApplication {

    public static void main(String[] args) {
        SpringApplication.run(EunstargramApplication.class, args);
    }

}
