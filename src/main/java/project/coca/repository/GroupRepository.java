package project.coca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.coca.domain.group.CoGroup;
import project.coca.domain.tag.Tag;

import java.util.List;

public interface GroupRepository extends JpaRepository<CoGroup, Long> {

    List<CoGroup> findByNameContaining(String groupName);

    @Query("SELECT g FROM CoGroup g JOIN g.groupMembers gm WHERE gm.groupMember.id = :groupMemberId")
    List<CoGroup> findByGroupMemberId(String groupMemberId);

    @Query("SELECT g FROM CoGroup g JOIN g.groupTags gt WHERE gt.tag = :tag")
    List<CoGroup> findByTagName(Tag tag);
}
