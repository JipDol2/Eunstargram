package jipdol2.eunstargram.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jipdol2.eunstargram.config.data.UserSession;
import jipdol2.eunstargram.exception.token.ExpiredToken;
import jipdol2.eunstargram.exception.token.InvalidToken;
import jipdol2.eunstargram.jwt.dto.UserSessionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;

@Slf4j
@Component
public class JwtManager {

    @Value("${jwt.secret}")
    private String KEY;

    private int ACCESS_TOKEN_TIME = 60*20*1000;

    private int REFRESH_TOKEN_TIME = 60*30*1000;

    public String makeAccessToken(Long id){
        return makeToken(id,ACCESS_TOKEN_TIME);
    }

    public String makeRefreshToken(Long id){
        return makeToken(id,REFRESH_TOKEN_TIME);
    }

    public String makeToken(Long id, int expiredTime){

        return Jwts.builder()
                .claim("id",id)
                .signWith(getSecurityKey())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+expiredTime))
                .compact();
    }

    public Long getMemberIdFromToken(String token){
        return Long.parseLong(String.valueOf(getClaims(token).get("id")));
    }

    private Claims getClaims(String token){
        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(getSecurityKey())
                .build()
                .parseClaimsJws(token);
        return claims.getBody();
    }

    private Key getSecurityKey(){
        return Keys.hmacShaKeyFor(Base64.getDecoder().decode(KEY));
    }

    public Long getExpirationTimeFromToken(String jws){
        byte[] decodedKey = Base64.getDecoder().decode(KEY);

        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(decodedKey)
                .build()
                .parseClaimsJws(jws);

        return claims.getBody().getExpiration().getTime();
    }

    public boolean validateToken(String token){
        try{
            Objects.requireNonNull(token);
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
        }catch(NullPointerException | JwtException | IllegalStateException e){
            log.error("token is not valid");
            throw new InvalidToken();
        }
    }
}
