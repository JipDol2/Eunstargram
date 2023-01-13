package jipdol2.eunstargram.article.entity;

import jipdol2.eunstargram.article.dto.PostDTO;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class PostRepository {

    @PersistenceContext
    private EntityManager em;

    public Long save(Post post){
        em.persist(post);
        return post.getSeq();
    }

    public Optional<Post> findByOne(Long seq){
        return Optional.ofNullable(em.find(Post.class,seq));
    }

    public List<PostDTO> findByAll(Long seq){
        return em.createQuery("SELECT new jipdol2.eunstargram.article.dto.PostDTO(a.seq,a.imagePath,a.likeNumber,a.content,m.seq) " +
                        "FROM Post a INNER JOIN a.member m WHERE m.seq = :seq")
                .setParameter("seq",seq)
                .getResultList();
    }

}
