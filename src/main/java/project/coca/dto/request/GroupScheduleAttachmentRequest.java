package project.coca.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.io.File;

@Getter
@Setter
@AllArgsConstructor
public class GroupScheduleAttachmentRequest {
    private String fileName;
    private String fileType;
    private File file;
}
