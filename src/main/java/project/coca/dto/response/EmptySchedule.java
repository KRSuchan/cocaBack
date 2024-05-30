package project.coca.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmptySchedule {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
