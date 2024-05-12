package project.coca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.coca.domain.group.GroupManager;

public interface GroupManagerRepository extends JpaRepository<GroupManager, Long> {
    @Query("DELETE FROM GroupManager gm WHERE gm.groupManager.id = :managerId AND gm.coGroup.id = :groupId")
    void deleteByManagerIdAndGroupId(String managerId, Long groupId);
}
