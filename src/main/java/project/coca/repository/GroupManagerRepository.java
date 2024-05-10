package project.coca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.coca.domain.group.GroupManager;

public interface GroupManagerRepository extends JpaRepository<GroupManager, Long> {
}
