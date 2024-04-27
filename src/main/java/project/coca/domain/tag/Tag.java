package project.coca.domain.tag;

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
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TAG_ID")
    private Long id;
    @Column(name = "FIELD")
    private String field;
    @Column(name = "NAME")
    private String name;

    // todo: onetomany group_tag
    // todo: onetomany interest
}
