package jipdol2.eunstargram.member.entity;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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

    public List<Member> findByMemberNickname(String nickname){
        return em.createQuery("SELECT m FROM Member m WHERE m.nickname = :nickname",Member.class)
                .setParameter("nickname",nickname)
                .getResultList();
    }

    public List<Member> findByMemberEmail(String memberEmail){
        return em.createQuery("SELECT m FROM Member m WHERE m.memberEmail = :memberEmail",Member.class)
                .setParameter("memberEmail",memberEmail)
                .getResultList();
    }

    public List<Member> findByAll(){
        return em.createQuery("SELECT m FROM Member m",Member.class)
                .getResultList();
    }

}