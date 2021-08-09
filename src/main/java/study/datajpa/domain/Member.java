package study.datajpa.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "username", "age"})
@NamedQuery(
        name = "Member.findByUsername",
        query = "select m from Member m where m.username =: username"
)
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String username;
    private int age;

    //다 : 1
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;
    
    //[이름, 나이, 팀] 생성자
    private Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        this.changeTeam(team);
    }

    //정적 팩토리 메서드
    public static Member createMember(String username, int age, Team team) {
        return new Member(username, age, team);
    }
    
    //연관관계 편의 메서드
    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }
}
