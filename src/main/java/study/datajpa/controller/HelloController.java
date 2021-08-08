package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.domain.Member;
import study.datajpa.domain.Team;
import study.datajpa.repository.MemberJpaRepository;
import study.datajpa.repository.MemberRepository;

@RestController
@RequiredArgsConstructor
public class HelloController {

    private final MemberRepository memberRepository;

    @Transactional
    @GetMapping("/hello")
    public String hello() {
        Team team = Team.createTeam("1팀");
        Member member = Member.createMember("김상운", 24, team);


        memberRepository.save(member);
        Member one = memberRepository.findById(member.getId()).orElse(null);

        return one.getUsername();
    }
}
