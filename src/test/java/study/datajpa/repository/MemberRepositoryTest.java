package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.domain.Member;
import study.datajpa.domain.Team;
import study.datajpa.dto.MemberDto;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class MemberRepositoryTest {
    @Autowired private MemberRepository memberRepository;
    @Autowired private TeamJpaRepository teamJpaRepository;
    @Autowired private TeamRepository teamRepository;
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

    @Test
    void paging() {
        Team team1 = Team.createTeam("teamA");
        Team team2 = Team.createTeam("teamB");
        teamRepository.save(team1);
        teamRepository.save(team2);

        Member save1 = memberRepository.save(Member.createMember("member1", 24, team1));
        Member save2 = memberRepository.save(Member.createMember("member2", 24, team1));
        Member save3 = memberRepository.save(Member.createMember("member3", 24, team1));
        Member save4 = memberRepository.save(Member.createMember("member4", 24, team1));
        Member save5 = memberRepository.save(Member.createMember("member5", 24, team1));

        int age = 24;

        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        Page<Member> page = memberRepository.findPageByAge(age, pageRequest);

        List<Member> content = page.getContent();

        for (Member member : content) {
            System.out.println("member = " + member);
        }

        //컨텐트의 사이즈
        assertThat(content.size()).isEqualTo(3);
        //모든 데이터의 수
        assertThat(page.getTotalElements()).isEqualTo(5);
        //현재 페이지의 수
        assertThat(page.getNumber()).isEqualTo(0);
        //페이지의 개수
        assertThat(page.getTotalPages()).isEqualTo(2);

        assertThat(page.isFirst()).isEqualTo(true);
        assertThat(page.hasNext()).isEqualTo(true);
    }

    @Test
    void bulkUpdate() {
        Team team1 = Team.createTeam("teamA");
        Team team2 = Team.createTeam("teamB");
        teamRepository.save(team1);
        teamRepository.save(team2);

        Member save1 = memberRepository.save(Member.createMember("member1", 18, team1));
        Member save2 = memberRepository.save(Member.createMember("member2", 19, team1));
        Member save3 = memberRepository.save(Member.createMember("member3", 22, team1));
        Member save4 = memberRepository.save(Member.createMember("member4", 23, team1));
        Member save5 = memberRepository.save(Member.createMember("member5", 24, team1));
        
        //벌크 연산은 영속성 컨텍스트를 무시하고 DB에 접근한다.
        //벌크연산을 수행해도 영속성 컨텍스트의 1차 캐쉬에는 아직 업데이트가 안됨
        //jpql은 쿼리 먼저 보내고 jpql 실행
        int i = memberRepository.bulkAgePlus(20);

        Member member = memberRepository.findByUsername("member5").get(0);
        System.out.println("member.getAge() = " + member.getAge());

        assertThat(i).isEqualTo(3);
    }

    @Test
    void findMemberLazy() {

        Team team1 = Team.createTeam("teamA");
        Team team2 = Team.createTeam("teamB");
        teamRepository.save(team1);
        teamRepository.save(team2);

        Member save1 = memberRepository.save(Member.createMember("member1", 18, team1));
        Member save2 = memberRepository.save(Member.createMember("member2", 19, team2));

        em.flush();
        em.clear();

        List<Member> memberFetchJoin = memberRepository.findMember2ByUsername("member1");

        for (Member member : memberFetchJoin) {
            System.out.println("member.getUsername() = " + member.getUsername());
            System.out.println("member.getTeam().getName() = " + member.getTeam().getName());
        }
    }

    @Test
    void readOnly() {
        Team team1 = Team.createTeam("teamA");
        Team team2 = Team.createTeam("teamB");
        teamRepository.save(team1);
        teamRepository.save(team2);

        Member save1 = memberRepository.save(Member.createMember("member1", 18, team1));
        Member save2 = memberRepository.save(Member.createMember("member2", 19, team2));

        em.flush();
        em.clear();

        Member findMember = memberRepository.findById(save1.getId()).orElse(null);
        findMember.setUsername("김상운");

        em.flush();
    }

    @Test
    void queryHint() {
        Team team1 = Team.createTeam("teamA");
        Team team2 = Team.createTeam("teamB");
        teamRepository.save(team1);
        teamRepository.save(team2);

        Member save1 = memberRepository.save(Member.createMember("member1", 18, team1));
        Member save2 = memberRepository.save(Member.createMember("member2", 19, team2));

        em.flush();
        em.clear();

        Member findMember = memberRepository.findReadOnlyByUsername("member1");
        findMember.setUsername("김상운");
        em.flush();
    }
    @Test
    void lock() {
        Team team1 = Team.createTeam("teamA");
        Team team2 = Team.createTeam("teamB");
        teamRepository.save(team1);
        teamRepository.save(team2);

        Member save1 = memberRepository.save(Member.createMember("member1", 18, team1));
        Member save2 = memberRepository.save(Member.createMember("member2", 19, team2));

        em.flush();
        em.clear();

        List<Member> findMember = memberRepository.findLockByUsername("member1");

    }

    @Test
    void callCustom() {
        Team team1 = Team.createTeam("teamA");
        Team team2 = Team.createTeam("teamB");
        teamRepository.save(team1);
        teamRepository.save(team2);

        Member save1 = memberRepository.save(Member.createMember("member1", 18, team1));
        Member save2 = memberRepository.save(Member.createMember("member2", 19, team2));

        List<Member> memberCustom = memberRepository.findMemberCustom();


    }


    @Test
    void projections() {
        Team team1 = Team.createTeam("teamA");
        Team team2 = Team.createTeam("teamB");
        teamRepository.save(team1);
        teamRepository.save(team2);

        Member save1 = memberRepository.save(Member.createMember("member1", 18, team1));
        Member save2 = memberRepository.save(Member.createMember("member2", 19, team2));

        List<UsernameOnly> member1 = memberRepository.findProByUsername("member1");

        for (UsernameOnly usernameOnly : member1) {
            System.out.println("usernameOnly = " + usernameOnly.getUsername());
        }

    }

    @Test
    void fkTest() {

        Team team1 = Team.createTeam("teamA");
        Team team2 = Team.createTeam("teamB");
        teamRepository.save(team1);
        teamRepository.save(team2);

        Member save1 = memberRepository.save(Member.createMember("member1", 18, team1));
        Member save2 = memberRepository.save(Member.createMember("member2", 19, team2));

        Member findMember = memberRepository.findByTeamId(team1.getId());

        assertThat(findMember).isEqualTo(save1);
    }

}
