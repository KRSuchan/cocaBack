package project.coca.dto.request;

import lombok.Getter;
import lombok.Setter;
import project.coca.domain.personal.Member;
import project.coca.domain.personal.PersonalSchedule;
import project.coca.domain.personal.PersonalScheduleAttachment;

import java.util.List;

@Getter
@Setter
public class PersonalScheduleRequest {
    private PersonalSchedule personalSchedule;
    private Member member;
    private List<PersonalScheduleAttachment> attachments;
}
