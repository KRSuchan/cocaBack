package project.coca.dto.response.group;

import lombok.Builder;
import lombok.Data;
import project.coca.domain.group.CoGroup;

@Data
@Builder
public class GroupResponse {
    private Long id;
    private String name;
    private String description;
    private Boolean isPrivate;

    public static GroupResponse of(CoGroup group) {
        return GroupResponse.builder()
                .id(group.getId())
                .name(group.getName())
                .description(group.getDescription())
                .isPrivate(group.getPrivatePassword() != null)
                .build();
    }
}
