package jipdol2.eunstargram.comment.entity;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CommentRepository {

    @PersistenceContext
    EntityManager em;

    public Long save(Comment comment){
        em.persist(comment);
        return comment.getId();
    }

    /**
     * Fetch Join 으로 최적화
     */
    public List<Comment> findByAllComment(Long postId){
        return em.createQuery("SELECT c FROM Comment c " +
                "INNER JOIN FETCH c.member " +
                "WHERE c.post.id = :id")
                .setParameter("id",postId)
                .getResultList();
    }


}
