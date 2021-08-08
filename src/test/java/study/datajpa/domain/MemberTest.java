package study.datajpa.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberTest {

    @Autowired private EntityManager em;

    @Test
    void 회원_팀_연관관계() {
        Team teamA = Team.createTeam("TeamA");
        Team teamB = Team.createTeam("TeamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = Member.createMember("member1", 1, teamA);
        Member member2 = Member.createMember("member2", 2, teamA);
        Member member3 = Member.createMember("member3", 3, teamB);
        Member member4 = Member.createMember("member4", 4, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        em.flush();
        em.clear();

        List<Member> memberList = em.createQuery("select m from Member m", Member.class).getResultList();

        for (Member member : memberList) {
            System.out.println("member = " + member);
            System.out.println("-> member.team = " + member.getTeam());
        }
    }
}