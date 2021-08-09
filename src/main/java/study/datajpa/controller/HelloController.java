package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.domain.Member;
import study.datajpa.domain.Team;
import study.datajpa.repository.MemberJpaRepository;
import study.datajpa.repository.MemberRepository;
import study.datajpa.repository.TeamRepository;
import study.datajpa.service.MemberService;
import study.datajpa.service.TeamService;

@RestController
@RequiredArgsConstructor
public class HelloController {

    private final MemberService memberService;
    private final TeamService teamService;

    @GetMapping("/hello")
    public String hello() {
        Team team = Team.createTeam("1팀");

        Member member = Member.createMember("김상운", 24, team);

        Long teamId = teamService.join(team);
        Long memberId = memberService.join(member);

        Member findMember = memberService.findMember(memberId);
        Team findTeam = teamService.findTeam(teamId);

        if (findMember.getTeam()==findTeam) {
            return "true";
        }

        return "false";
    }
}
