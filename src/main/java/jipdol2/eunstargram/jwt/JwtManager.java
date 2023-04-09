package jipdol2.eunstargram.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jipdol2.eunstargram.exception.ExpiredToken;
import jipdol2.eunstargram.exception.InvalidToken;
import jipdol2.eunstargram.jwt.dto.UserSessionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JwtManager {

    @Value("${jwt.secret}")
    private String KEY;

    private static final int ACCESS_TOKEN = 60*20*1000;      //20분

    private static final int REFRESH_TOKEN = 60*30*1000;    //30분

    public String makeToken(UserSessionDTO sessionDTO, String type){

        SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(KEY));

        int expiredTime = type.equals("ACCESS") ? ACCESS_TOKEN : REFRESH_TOKEN;

        Claims claims = Jwts.claims();
        claims.put("id",sessionDTO.getId());
        claims.put("email",sessionDTO.getEmail());
        claims.put("nickname",sessionDTO.getNickname());

        return Jwts.builder()
                .setClaims(claims)
                .signWith(key)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+expiredTime))
                .compact();
    }

    public UserSessionDTO getMemberIdFromToken(String jws){
        byte[] decodedKey = Base64.getDecoder().decode(KEY);

        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(decodedKey)
                .build()
                .parseClaimsJws(jws);

        return UserSessionDTO.builder()
                .id(Long.parseLong(String.valueOf(claims.getBody().get("id"))))
                .email(String.valueOf(claims.getBody().get("email")))
                .nickname(String.valueOf(claims.getBody().get("nickname")))
                .build();
    }

    public boolean validateToken(String token){
        try{
            byte[] decodedKey = Base64.getDecoder().decode(KEY);
            Jwts.parserBuilder()
                    .setSigningKey(decodedKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        }catch (ExpiredJwtException e){
            /**
             * 만약 accessToken 이 만료되었다면 해당 exception이 발생
             * 그러면 클라이언트에게 error message를 던지고 클라이언트는 accessToken 재발급 api 를 호출
             */
            log.error("token is expired");
            throw new ExpiredToken();
        }
    }
}
