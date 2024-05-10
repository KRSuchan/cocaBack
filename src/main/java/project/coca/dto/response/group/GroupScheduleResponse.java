package project.coca.dto.response.group;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import project.coca.domain.group.GroupSchedule;
import project.coca.domain.group.GroupScheduleAttachment;
import project.coca.dto.response.personalSchedule.PersonalScheduleAttachmentResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@Setter
public class GroupScheduleResponse {
    private Long id;
    private String title;
    private String description;
    private String location;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
    private String color;
    private List<GroupScheduleAttachmentResponse> groupScheduleAttachments;

    public static GroupScheduleResponse of(GroupSchedule groupSchedule) {
        return GroupScheduleResponse.builder()
                .id(groupSchedule.getId())
                .title(groupSchedule.getTitle())
                .description(groupSchedule.getDescription())
                .location(groupSchedule.getLocation())
                .startTime(groupSchedule.getStartTime())
                .endTime(groupSchedule.getEndTime())
                .color(groupSchedule.getColor())
                .groupScheduleAttachments(groupSchedule.getGroupScheduleAttachments().stream()
                        .map(GroupScheduleAttachmentResponse::of)
                        .collect(Collectors.toList()))
                .build();
    }
}
