package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.domain.Member;
import study.datajpa.domain.Team;
import study.datajpa.dto.MemberDto;
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

    @GetMapping("/members")
    public Page<MemberDto> list(@PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return memberRepository.findAll(pageable).map(MemberDto::new);
    }

    @PostConstruct
    public void init() {
        Team team1 = Team.createTeam("teamA");
        Team team2 = Team.createTeam("teamB");
        teamRepository.save(team1);
        teamRepository.save(team2);

        Member save1 = memberRepository.save(Member.createMember("member1", 20, team1));
        Member save2 = memberRepository.save(Member.createMember("member2", 21, team1));
        Member save3 = memberRepository.save(Member.createMember("member3", 22, team1));
        Member save4 = memberRepository.save(Member.createMember("member4", 23, team1));
        Member save5 = memberRepository.save(Member.createMember("member5", 24, team1));
        Member save = memberRepository.save(Member.createMember("member6", 20, team1));
        Member member = memberRepository.save(Member.createMember("member7", 21, team1));
        Member member1 = memberRepository.save(Member.createMember("member8", 22, team1));
        Member member2 = memberRepository.save(Member.createMember("member9", 23, team1));
        Member member3 = memberRepository.save(Member.createMember("member10", 24, team1));
        Member member4 = memberRepository.save(Member.createMember("member11", 20, team1));
        Member member5 = memberRepository.save(Member.createMember("member12", 21, team1));
        Member member6 = memberRepository.save(Member.createMember("member13", 22, team1));
        Member member7 = memberRepository.save(Member.createMember("member14", 23, team1));
        Member member8 = memberRepository.save(Member.createMember("member15", 24, team1));
        Member member9 = memberRepository.save(Member.createMember("member16", 20, team1));
        Member member10 = memberRepository.save(Member.createMember("member17", 21, team1));
        Member member11 = memberRepository.save(Member.createMember("member18", 22, team1));
        Member member12 = memberRepository.save(Member.createMember("member19", 23, team1));
        Member member13 = memberRepository.save(Member.createMember("member20", 24, team1));
    }
}
