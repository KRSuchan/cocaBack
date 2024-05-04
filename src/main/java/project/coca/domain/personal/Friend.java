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
    @Column(name = "MEMBER1_NICKNAME", nullable = false, length = 20)
    private String member1Nickname;
    @Column(name = "MEMBER2_NICKNAME", nullable = false, length = 20)
    private String member2Nickname;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER1_ID", nullable = false)
    private Member member1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER2_ID", nullable = false)
    private Member member2;
}
