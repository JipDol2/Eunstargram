package jipdol2.eunstargram.article.entity;

import jipdol2.eunstargram.article.dto.ArticleDTO;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class ArticleRepository {

    @PersistenceContext
    private EntityManager em;

    public Long save(Article article){
        em.persist(article);
        return article.getSeq();
    }

    public Optional<Article> findByOne(Long seq){
        return Optional.ofNullable(em.find(Article.class,seq));
    }

    public List<ArticleDTO> findByAll(Long seq){
        return em.createQuery("SELECT new jipdol2.eunstargram.article.dto.ArticleDTO(a.seq,a.imagePath,a.likeNumber,a.content,m.seq) " +
                        "FROM Article a INNER JOIN a.member m WHERE m.seq = :seq")
                .setParameter("seq",seq)
                .getResultList();
    }

}
