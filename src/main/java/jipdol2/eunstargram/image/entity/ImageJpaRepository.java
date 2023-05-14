package jipdol2.eunstargram.image.entity;

import jipdol2.eunstargram.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface ImageJpaRepository extends JpaRepository<Image,Long> {

//    @Query("SELECT i FROM Image i WHERE i.member.id = :id AND i.imageCode = :imageCode")
//    Optional<Image> findImageByMemberId(@Param("id") Long id, @Param("imageCode") ImageCode imageCode);

    @Query("SELECT i FROM Image i WHERE i.storedFileName = :storedFileName AND i.imageCode = :imageCode")
    Optional<Image> findImageByStoredFileName(@Param("storedFileName") String storedFileName, @Param("imageCode")ImageCode imageCode);
}
