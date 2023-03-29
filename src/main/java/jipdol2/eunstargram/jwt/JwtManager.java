package jipdol2.eunstargram.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtManager {

    @Value("${jwt.secret}")
    private String KEY;

    private static final int ACCESS_TOKEN = 60*5*1000;      //5분

    private static final int REFRESH_TOKEN = 60*30*1000;    //30분

    public String makeAccessToken(Long id, String type){

        SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(KEY));

        int expiredTime = type.equals("ACCESS") ? ACCESS_TOKEN : REFRESH_TOKEN;
        return Jwts.builder()
                .setSubject(String.valueOf(id))
                .signWith(key)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+expiredTime))
                .compact();
    }
}
