package jipdol2.eunstargram.member.entity;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberRepository {

    @PersistenceContext
    private EntityManager em;

    public Long save(Member member){
        em.persist(member);
        return member.getSeq();
    }

    public Optional<Member> findByOne(Long seq){
        return Optional.ofNullable(em.find(Member.class,seq));
    }

    public List<Member> findByOneId(String id){
        return em.createQuery("SELECT m FROM Member m WHERE m.memberId = :id",Member.class)
                .setParameter("id",id)
                .getResultList();
    }

    public List<Member> findByAll(){
        return em.createQuery("SELECT m FROM Member m",Member.class)
                .getResultList();
    }

}
