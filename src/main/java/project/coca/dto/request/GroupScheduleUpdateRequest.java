package project.coca.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.coca.domain.group.GroupSchedule;
import project.coca.domain.group.GroupScheduleAttachment;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GroupScheduleUpdateRequest {
    private String memberId; //수정을 시도한 멤버 아이디
    private Long scheduleId;
    private Long groupId;
    private String title;
    private String description;
    private String location;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
    private String color;
    private List<GroupScheduleAttachmentRequest> attachments;
}
