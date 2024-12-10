package project.coca.v1.dto.request;

import lombok.Getter;
import lombok.Setter;
import project.coca.v1.domain.personal.Member;
import project.coca.v1.domain.request.RequestedSchedule;

import java.util.List;

@Setter
@Getter
public class ScheduleRequestRequest {
    private Member sender;
    private RequestedSchedule requestedSchedule;
    private List<Member> receivers;
}
