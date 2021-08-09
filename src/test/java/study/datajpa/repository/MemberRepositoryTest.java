package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.domain.Member;
import study.datajpa.domain.Team;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(false)
public class MemberRepositoryTest {
    @Autowired private MemberRepository memberRepository;
    @Autowired private TeamJpaRepository teamJpaRepository;

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

}
