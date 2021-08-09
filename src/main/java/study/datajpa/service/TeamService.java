package study.datajpa.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.domain.Team;
import study.datajpa.repository.TeamRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;

    @Transactional
    public Long join(Team team) {
        Team saveTeam = teamRepository.save(team);
        return saveTeam.getId();
    }

    public Team findTeam(Long id) {
        Team team = teamRepository.findById(id).orElse(null);
        return team;
    }
}
