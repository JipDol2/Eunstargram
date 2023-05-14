package jipdol2.eunstargram.auth.entity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken,Long> {

    Optional<RefreshToken> findByRefreshToken(@Param("refreshToken")String refreshToken);

}
