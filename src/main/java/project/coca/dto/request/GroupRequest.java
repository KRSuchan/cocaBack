package project.coca.dto.request;

import lombok.Getter;
import lombok.Setter;
import project.coca.domain.group.CoGroup;
import project.coca.domain.personal.Member;

@Getter
@Setter
public class GroupRequest {
    private Member member;
    private CoGroup group;
}
