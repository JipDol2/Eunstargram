package jipdol2.eunstargram.crypto;

import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/crypto/scrypt/SCryptPasswordEncoder.html
 */
@Component
public class MyPasswordEncoder {

    private final SCryptPasswordEncoder encoder =
            new SCryptPasswordEncoder(32,8,1,32,64);

    public String encrypt(String password){
        return encoder.encode(password);
    }

    public boolean matcher(String rawPassword,String encryptedPassword){
        return encoder.matches(rawPassword,encryptedPassword);
    }
}