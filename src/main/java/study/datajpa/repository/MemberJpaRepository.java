package study.datajpa.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.datajpa.domain.Member;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberJpaRepository {

    private final EntityManager em;
    
    //저장
    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    //단건 조회
    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    //삭제
    public void delete(Member member) {
        em.remove(member);
    }

    //전체 조회
    public List<Member> findAll() {
        return em.createQuery("select M from Member m", Member.class).
                getResultList();
    }

    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    public long count() {
        return em.createQuery("select count(m) from Member m", Long.class).getSingleResult();
    }
}
