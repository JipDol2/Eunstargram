package jipdol2.eunstargram.member.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

//@Repository
public interface MemberJpaRepository extends JpaRepository<Member,Long> {

    public Optional<Member> findByMemberIdAndPassword(@Param("userId") String userId, @Param("password")String password);
}
