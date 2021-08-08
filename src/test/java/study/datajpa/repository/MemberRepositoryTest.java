package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.domain.Member;

import java.util.List;

@SpringBootTest
@Transactional
@Rollback(false)
public class MemberRepositoryTest {
    @Autowired private MemberRepository memberRepository;

    @Test
    void 회원_저장() {
        Member member = new Member("김상운");
        Member saveMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(member.getId()).orElse(null);
        List<Member> all = memberRepository.findAll();

        //인터페이스인데 어떻게 동작하지..?
        Assertions.assertThat(member).isEqualTo(findMember);
    }
}
