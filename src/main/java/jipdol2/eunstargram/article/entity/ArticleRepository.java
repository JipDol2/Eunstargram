package jipdol2.eunstargram.article.entity;

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

    public List<Article> findByArticle(String userId){
        return em.createQuery("SELECT a FROM Article a LEFT JOIN Member m WHERE m.userId = :userId")
                .getResultList();
    }
}
