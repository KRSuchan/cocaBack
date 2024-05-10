package project.coca.dto.request;

import lombok.Getter;
import lombok.Setter;
import project.coca.domain.group.GroupSchedule;
import project.coca.domain.group.GroupScheduleAttachment;

import java.util.List;

@Getter
@Setter
public class GroupScheduleRequest {
    private GroupSchedule groupSchedule;
    private List<GroupScheduleAttachment> attachments;
}
