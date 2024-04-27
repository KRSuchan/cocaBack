package project.coca.domain.request;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
public class ScheduleRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SCHEDULE_REQUEST_ID")
    private Long id;
    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus;
    @CreatedDate
    @Column(updatable = false, name = "CREATED_AT")
    private LocalDateTime createdDate;

    // todo: manytoone member for sender
    // todo: manytoone member for reciever
    // todo: manytoone requested_schedule

}
