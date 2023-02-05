package jipdol2.eunstargram.image.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostImageJpaRepository extends JpaRepository<PostImage,Long> {

    @Query("SELECT p FROM PostImage p WHERE p.member.memberId=:memberId")
    public List<PostImage> findByMemberId(@Param("memberId") Long memberId);
}
