package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.domain.Member;
import study.datajpa.domain.Team;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberJpaRepositoryTest {

    @Autowired
    private MemberJpaRepository memberJpaRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TeamJpaRepository teamRepository;
    @Autowired
    private EntityManager em;

    @Test
    public void basicCRUD() {
        Team team1 = Team.createTeam("teamA");
        Team team2 = Team.createTeam("teamB");
        teamRepository.save(team1);
        teamRepository.save(team2);

        Member member1 = Member.createMember("memberA", 24, team1);
        Member member2 = Member.createMember("memberB", 27, team1);
        Member save1 = memberJpaRepository.save(member1);
        Member save2 = memberJpaRepository.save(member2);

        assertThat(member1).isEqualTo(memberJpaRepository.findById(save1.getId()).get());
        assertThat(member2).isEqualTo(memberJpaRepository.findById(save2.getId()).get());

        long count = memberJpaRepository.count();

        assertThat(2).isEqualTo(count);
    }

    @Test
    void findByUserNameAndAgeGreaterThan() {
        Team team1 = Team.createTeam("teamA");
        Team team2 = Team.createTeam("teamB");
        teamRepository.save(team1);
        teamRepository.save(team2);

        Member member1 = Member.createMember("memberA", 24, team1);
        Member member2 = Member.createMember("memberB", 27, team1);
        Member save1 = memberJpaRepository.save(member1);
        Member save2 = memberJpaRepository.save(member2);

        List<Member> memberA = memberRepository.findByUsernameAndAgeGreaterThanEqual("memberA", 20);
        for (Member member : memberA) {
            System.out.println("member = " + member);
        }
    }

    @Test
    void paging() {
        Team team1 = Team.createTeam("teamA");
        Team team2 = Team.createTeam("teamB");
        teamRepository.save(team1);
        teamRepository.save(team2);

        Member save1 = memberJpaRepository.save(Member.createMember("member1", 24, team1));
        Member save2 = memberJpaRepository.save(Member.createMember("member2", 24, team1));
        Member save3 = memberJpaRepository.save(Member.createMember("member3", 24, team1));
        Member save4 = memberJpaRepository.save(Member.createMember("member4", 24, team1));
        Member save5 = memberJpaRepository.save(Member.createMember("member5", 24, team1));

        int age = 24;
        int offset = 1;
        int limit = 3;

        List<Member> members = memberJpaRepository.findByPage(age, offset, limit);
        long total = memberJpaRepository.totalCount(age);

        assertThat(members.size()).isEqualTo(3);
        assertThat(total).isEqualTo(5);
    }

    @Test
    void bulkUpdate() {
        Team team1 = Team.createTeam("teamA");
        Team team2 = Team.createTeam("teamB");
        teamRepository.save(team1);
        teamRepository.save(team2);

        Member save1 = memberJpaRepository.save(Member.createMember("member1", 18, team1));
        Member save2 = memberJpaRepository.save(Member.createMember("member2", 19, team1));
        Member save3 = memberJpaRepository.save(Member.createMember("member3", 22, team1));
        Member save4 = memberJpaRepository.save(Member.createMember("member4", 23, team1));
        Member save5 = memberJpaRepository.save(Member.createMember("member5", 24, team1));

        int i = memberJpaRepository.bulkAgePlus(20);

        assertThat(i).isEqualTo(3);
    }
    @Test
    void jpqlTest() {
        Team team1 = Team.createTeam("teamA");
        Team team2 = Team.createTeam("teamB");
        teamRepository.save(team1);
        teamRepository.save(team2);

        Member save1 = memberJpaRepository.save(Member.createMember("member1", 18, team1));
        Member save2 = memberJpaRepository.save(Member.createMember("member2", 19, team2));

        System.out.println("///////////////////////////////");
        memberJpaRepository.findOneJpql(save1.getId());
        System.out.println("///////////////////////////////");
    }

    @Test
    void JpaBaseEntity() throws InterruptedException {
        Team team1 = Team.createTeam("teamA");
        teamRepository.save(team1);

        Member member = Member.createMember("member1", 18, team1);
        memberRepository.save(member);

        Thread.sleep(1000);
        member.setUsername("김상운");

        Member findMember = memberRepository.findById(member.getId()).orElse(null);
        em.flush();
        em.clear();

        System.out.println("findMember.getc = " + findMember.getCreatedDate());
        System.out.println("findMember.getUpdateDate() = " + findMember.getLastModifiedDate());
        System.out.println("findMember.getCreatedBy() = " + findMember.getCreatedBy());
        System.out.println("findMember.getLastModifiedBy() = " + findMember.getLastModifiedBy());
    }
}