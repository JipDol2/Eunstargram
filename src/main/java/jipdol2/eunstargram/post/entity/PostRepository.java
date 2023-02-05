package jipdol2.eunstargram.post.entity;

import jipdol2.eunstargram.post.dto.response.PostResponseDTO;
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
        return post.getId();
    }

    public Optional<Post> findByOne(Long seq){
        return Optional.ofNullable(em.find(Post.class,seq));
    }

    public List<PostResponseDTO> findByAll(Long id){
        return em.createQuery("SELECT new jipdol2.eunstargram.post.dto.response.PostResponseDTO(a.id,a.likeNumber,a.content,m.id) " +
                        "FROM Post a INNER JOIN a.member m WHERE m.id = :id")
                .setParameter("id",id)
                .getResultList();
    }

    public List<PostResponseDTO> findByAll(String memberId){
        return em.createQuery("SELECT new jipdol2.eunstargram.post.dto.response.PostResponseDTO(p.id,p.likeNumber,p.content,m.id) " +
                        "FROM Post p INNER JOIN p.member m WHERE m.memberId = :memberId")
                .setParameter("memberId",memberId)
                .getResultList();
    }

}
