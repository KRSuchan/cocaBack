package project.coca.domain.personal;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
public class Member {
    @Id
    @Column(name = "MEMBER_ID")
    private String id;

    @Column(name = "PASSWORD")
    private String password;
    @Column(name = "USERNAME")
    private String userName;
    @Column(name = "PROFILE_IMG_PATH")
    private String profileImgPath;

    // todo: onetomany group (for creator)
    // todo: onetomany groupMember (for attendee)
    // todo: onetomany groupManager
    // todo: onetomany personal_schedule
    // todo: onetomany groupRequest
    // todo: onetomany scheduleRequest
    // todo: onetomany friendRequest
    // todo: onetomany friend
    // todo: onetomany interest

}
