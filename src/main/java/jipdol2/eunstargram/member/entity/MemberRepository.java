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
        return member.getId();
    }

    public Optional<Member> findByOne(Long id){
        return Optional.ofNullable(em.find(Member.class,id));
    }

    public List<Member> findByMemberId(String memberId){
        return em.createQuery("SELECT m FROM Member m WHERE m.memberId = :memberId",Member.class)
                .setParameter("memberId",memberId)
                .getResultList();
    }

    public List<Member> findByAll(){
        return em.createQuery("SELECT m FROM Member m",Member.class)
                .getResultList();
    }

}