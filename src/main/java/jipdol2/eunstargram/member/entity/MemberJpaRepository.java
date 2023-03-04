package jipdol2.eunstargram.member.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<Member,Long> {

    public Optional<Member> findByMemberEmailAndPassword(@Param("userEmail") String userEmail, @Param("password")String password);
}
