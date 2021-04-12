package study.datajpa.entity;

import lombok.*;

import javax.persistence.*;

import java.util.Collection;

import static javax.persistence.FetchType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter @Setter
@NoArgsConstructor
//@ToString(of = {"id", "username", "age"}) // "team"까지 추가로 적으면 연관관계까지 끌고온다.
@ToString(exclude = "team")
@NamedQuery(
    name="Member.findByUsername",
    query="select m from Member m where m.username = :username"
)
@NamedEntityGraph(name = "Member.all", attributeNodes = @NamedAttributeNode("team"))
public class Member extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;
    private int age;

//    protected Member() { }

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public Member(String username) {
        this.username = username;
    }

    public Member(String username, int age) {
        this.username = username;
        this.age = age;
    }

    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        if (team != null) {
            changeTeam(team);
        }
    }

    //Setter를 없애는 경우 이런 방식으로 메서드가 있어야됨
    public void changeUserName(String username) {
        this.username = username;
    }

    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }
}
