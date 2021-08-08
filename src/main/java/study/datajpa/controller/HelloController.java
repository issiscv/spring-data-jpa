package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.domain.Member;
import study.datajpa.repository.MemberJpaRepository;
import study.datajpa.repository.MemberRepository;

@RestController
@RequiredArgsConstructor
public class HelloController {

    private final MemberRepository memberRepository;

    @Transactional
    @GetMapping("/hello")
    public String hello() {
        Member member = new Member();
        member.setUsername("김상운");

        memberRepository.save(member);
        Member one = memberRepository.findById(member.getId()).orElse(null);

        return one.getUsername();
    }
}
