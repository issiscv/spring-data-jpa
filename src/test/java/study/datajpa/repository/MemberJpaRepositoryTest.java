package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.domain.Member;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

    @Autowired
    private MemberJpaRepository memberJpaRepository;

//    @Test
//    void 회원_저장() {
//        Member member = Member.createMember("김상운");
//
//        Long memberId = memberJpaRepository.save(member);
//
//        assertThat(member).isEqualTo(memberJpaRepository.findOne(memberId));
//        assertThat(member.getUsername()).isEqualTo(memberJpaRepository.findOne(memberId).getUsername());
//    }
}