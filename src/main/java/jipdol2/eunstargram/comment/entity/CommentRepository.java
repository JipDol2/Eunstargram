package jipdol2.eunstargram.comment.entity;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class CommentRepository {

    @PersistenceContext
    EntityManager em;

    public Long save(Comment comment){
        em.persist(comment);
        return comment.getId();
    }
}
