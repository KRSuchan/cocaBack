package project.coca.domain.personal;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FRIEND_ID")
    private Long id;
    @Column(name = "MEMBER1_NICKNAME")
    private String member1Nickname;
    @Column(name = "MEMBER2_NICKNAME")
    private String member2Nickname;

    // todo: manytoone Member (member1)
    // todo: manytoone Member (member1)
}
