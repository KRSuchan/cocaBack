package project.coca.domain.request;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import project.coca.domain.group.CoGroup;
import project.coca.domain.personal.Member;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
public class GroupRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GROUP_REQUEST_ID")
    private Long id;
    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus;
    @CreatedDate
    @Column(updatable = false, name = "CREATED_AT")
    private LocalDateTime createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GROUP_ID", nullable = false)
    private CoGroup coGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SENDER_ID", nullable = false)
    private Member sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RECEIVER_ID", nullable = false)
    private Member receiver;
}
