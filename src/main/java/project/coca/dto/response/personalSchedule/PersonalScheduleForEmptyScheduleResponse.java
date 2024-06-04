package project.coca.dto.response.personalSchedule;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.coca.dto.response.CommonSchedule;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonalScheduleForEmptyScheduleResponse {
    String memberId;
    String memberName;
    List<CommonSchedule> scheduleList = new ArrayList<>();
}
