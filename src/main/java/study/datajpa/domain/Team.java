package study.datajpa.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "name"})
public class Team {

    @Id @GeneratedValue
    @Column(name = "team_id")
    private Long id;

    private String name;
    
    //1 : 다
    @OneToMany(mappedBy = "team")
    private List<Member> members = new ArrayList<>();

    //[이름] 생성자
    private Team(String name) {
        this.name = name;
    }

    //정적 팩토리 메서드
    public static Team createTeam(String name) {
        return new Team(name);
    }
}
