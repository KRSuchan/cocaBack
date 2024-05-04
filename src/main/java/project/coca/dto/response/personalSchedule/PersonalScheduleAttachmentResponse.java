package project.coca.dto.response.personalSchedule;

import lombok.Builder;
import lombok.Data;
import project.coca.domain.personal.PersonalScheduleAttachment;

@Data
@Builder
public class PersonalScheduleAttachmentResponse {
    private String fileName;
    private String filePath;

    public static PersonalScheduleAttachmentResponse of(PersonalScheduleAttachment attachment) {
        return PersonalScheduleAttachmentResponse.builder()
                .fileName(attachment.getFileName())
                .filePath(attachment.getFilePath())
                .build();
    }
}
