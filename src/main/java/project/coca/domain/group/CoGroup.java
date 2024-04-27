package project.coca.domain.group;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
public class CoGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GROUP_ID")
    private Long id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "PRIVATE_PASSWORD")
    private String privatePassword;

    // todo: OneToMany group_admin_id (<- Member class)
    // todo: onetoone group_notice
    // todo: onetomany group_request
    // todo: onetomany group_tag
    // todo: onetomany group_manager
    // todo: onetomany group_member
    // todo: onetomany group_schedule

}
