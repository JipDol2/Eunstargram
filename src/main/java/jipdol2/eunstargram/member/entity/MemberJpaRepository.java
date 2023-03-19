package jipdol2.eunstargram.member.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<Member,Long> {


    Optional<Member> findByMemberEmailAndPassword(@Param("email") String email,@Param("password") String password);

    Optional<Member> findByNickname(@Param("nickname") String nickname);
}
