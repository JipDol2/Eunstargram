package jipdol2.eunstargram.auth.entity;

import jipdol2.eunstargram.auth.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SessionJpaRepository extends JpaRepository<Session,Long> {

    Optional<Session> findByAccessToken(String accessToken);
}
