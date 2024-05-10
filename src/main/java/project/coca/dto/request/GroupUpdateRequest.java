package project.coca.dto.request;

import lombok.Getter;
import lombok.Setter;
import project.coca.domain.group.GroupManager;

import java.util.List;

@Getter
@Setter
public class GroupUpdateRequest {
    private Long groupId;
    private String adminId;
    private String groupName;
    private String description;
    private List<GroupManager> managers;
}
