package project.coca.dto.request;

import lombok.Getter;
import lombok.Setter;
import project.coca.domain.group.GroupManager;
import project.coca.domain.group.GroupMember;
import project.coca.domain.request.RequestedSchedule;

import java.util.List;

@Setter
@Getter
public class ScheduleRequestToGroupMember {
    private Long groupId;
    private GroupManager manager;
    private RequestedSchedule requestedSchedule;
    private List<GroupMember> groupMembers;
}
