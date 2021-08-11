package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.domain.Member;
import study.datajpa.domain.Team;
import study.datajpa.repository.MemberRepository;
import study.datajpa.repository.TeamRepository;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable Long id) {
        Member member = memberRepository.findById(id).orElse(null);
        return member.getUsername();
    }

    @GetMapping("/members2/{id}")
    public String findMemberV2(@PathVariable("id")  Member member) {
        return member.getUsername();
    }

    @PostConstruct
    public void init() {
        Team team1 = teamRepository.save(Team.createTeam("team1"));
        memberRepository.save(Member.createMember("김상운", 24, team1));
    }
}
