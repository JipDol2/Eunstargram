package jipdol2.eunstargram.member.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<Member,Long> {

    Optional<Member> findByMemberEmail(@Param("email") String email);

    Optional<Member> findByMemberEmailAndPassword(@Param("email") String email,@Param("password") String password);

    Optional<Member> findByNickname(@Param("nickname") String nickname);

    @Query("SELECT m FROM Member m WHERE m.socialId = :socialId")
    Optional<Member> findBySocialId(@Param("socialId") Long socialId);
}
