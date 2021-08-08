package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.domain.Member;
import study.datajpa.domain.Team;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

    @Autowired
    private MemberJpaRepository memberJpaRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TeamJpaRepository teamRepository;

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
}