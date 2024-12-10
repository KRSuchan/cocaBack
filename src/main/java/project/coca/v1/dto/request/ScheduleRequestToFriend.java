package project.coca.v1.dto.request;

import lombok.Getter;
import lombok.Setter;
import project.coca.v1.domain.personal.Friend;
import project.coca.v1.domain.personal.Member;
import project.coca.v1.domain.request.RequestedSchedule;

import java.util.List;

@Setter
@Getter
public class ScheduleRequestToFriend {
    private Member member;
    private RequestedSchedule requestedSchedule;
    private List<Friend> friends;
}
