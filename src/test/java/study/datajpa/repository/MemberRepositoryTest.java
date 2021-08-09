package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.domain.Member;
import study.datajpa.domain.Team;
import study.datajpa.dto.MemberDto;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(false)
public class MemberRepositoryTest {
    @Autowired private MemberRepository memberRepository;
    @Autowired private TeamJpaRepository teamJpaRepository;
    @Autowired private MemberJpaRepository memberJpaRepository;

    @Autowired private EntityManager em;
    @Test
    public void basicCRUD() {
        Team team1 = Team.createTeam("teamA");
        Team team2 = Team.createTeam("teamB");
        teamJpaRepository.save(team1);
        teamJpaRepository.save(team2);

        Member member1 = Member.createMember("memberA", 24, team1);
        Member member2 = Member.createMember("memberA", 24, team1);
        Member save1 = memberRepository.save(member1);
        Member save2 = memberRepository.save(member2);

        Member findMember = memberRepository.findByUsername("memberA").get(0);

        assertThat(member1).isEqualTo(memberRepository.findById(save1.getId()).get());
        assertThat(member2).isEqualTo(memberRepository.findById(save2.getId()).get());
        assertThat(findMember).isEqualTo(member1);

        long count = memberRepository.count();

        assertThat(2).isEqualTo(count);
    }

    @Test
    void testQuery() {
        Team team1 = Team.createTeam("teamA");
        Team team2 = Team.createTeam("teamB");
        teamJpaRepository.save(team1);
        teamJpaRepository.save(team2);

        Member member1 = Member.createMember("memberA", 24, team1);
        Member member2 = Member.createMember("memberA", 24, team1);
        Member save1 = memberRepository.save(member1);
        Member save2 = memberRepository.save(member2);

        List<Member> memberA = memberRepository.findUser("memberA", 24);

        assertThat(memberA.get(0)).isEqualTo(member1);
    }
    @Test
    void findUsernameList() {
        Team team1 = Team.createTeam("teamA");
        Team team2 = Team.createTeam("teamB");
        teamJpaRepository.save(team1);
        teamJpaRepository.save(team2);

        Member member1 = Member.createMember("memberA", 24, team1);
        Member member2 = Member.createMember("memberB", 24, team1);
        Member save1 = memberRepository.save(member1);
        Member save2 = memberRepository.save(member2);

        List<String> usernameList = memberRepository.findUsernameList();
        for (String s : usernameList) {
            System.out.println("s = " + s);
        }
    }

    @Test
    void findMemberDto() {
        Team team1 = Team.createTeam("teamA");
        Team team2 = Team.createTeam("teamB");
        teamJpaRepository.save(team1);
        teamJpaRepository.save(team2);

        Member member1 = Member.createMember("memberA", 24, team1);
        Member member2 = Member.createMember("memberB", 24, team1);
        Member save1 = memberRepository.save(member1);
        Member save2 = memberRepository.save(member2);

        em.flush();
        em.clear();

        List<MemberDto> memberDto = memberRepository.findMemberDto();
        for (MemberDto dto : memberDto) {
            System.out.println(dto.getId() + " " + dto.getUsername() + " " + dto.getTeamName());
        }
    }

    @Test
    void findMember() {
        Team team1 = Team.createTeam("teamA");
        Team team2 = Team.createTeam("teamB");
        teamJpaRepository.save(team1);
        teamJpaRepository.save(team2);

        Member member1 = Member.createMember("memberA", 24, team1);
        Member member2 = Member.createMember("memberB", 24, team2);
        Member save1 = memberRepository.save(member1);
        Member save2 = memberRepository.save(member2);

        em.flush();
        em.clear();

        List<Member> members = memberRepository.findMember();
        for (Member member : members) {
            System.out.println(member.getTeam().getName());
        }
    }

    @Test
    void findByNames() {
        Team team1 = Team.createTeam("teamA");
        Team team2 = Team.createTeam("teamB");
        teamJpaRepository.save(team1);
        teamJpaRepository.save(team2);

        Member member1 = Member.createMember("memberA", 24, team1);
        Member member2 = Member.createMember("memberB", 24, team2);
        Member save1 = memberRepository.save(member1);
        Member save2 = memberRepository.save(member2);

        List<Member> findMembers = memberRepository.findByNames(Arrays.asList("memberA", "memberB"));

        for (Member findMember : findMembers) {
            System.out.println("findMember = " + findMember);
        }
    }

    @Test
    void returnType() {
        Team team1 = Team.createTeam("teamA");
        Team team2 = Team.createTeam("teamB");
        teamJpaRepository.save(team1);
        teamJpaRepository.save(team2);

        Member member1 = Member.createMember("memberA", 24, team1);
        Member member2 = Member.createMember("memberB", 24, team2);
        Member save1 = memberRepository.save(member1);
        Member save2 = memberRepository.save(member2);

        //단건 조회 시 데이터가 없으면, 순수 jpa에서는 NPE, spring data jpa 는 null 반환
        //-> optional로 해결
        memberRepository.findListByUsername("memberA");
    }
}
