package project.coca.dto.response.group;

import lombok.Builder;
import lombok.Data;
import project.coca.domain.group.CoGroup;
import project.coca.domain.personal.Member;

@Data
@Builder
public class CalendarResponse {
    private Long groupId;
    private String groupName;
    private Boolean isAdmin;

    public static CalendarResponse of(CoGroup group, Member member) {
        return CalendarResponse.builder()
                .groupId(group.getId())
                .groupName(group.getName())
                .isAdmin(group.getAdmin().getId().equals(member.getId()))
                .build();
    }
}
