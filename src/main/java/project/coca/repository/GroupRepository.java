package project.coca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.coca.domain.group.CoGroup;

public interface GroupRepository extends JpaRepository<CoGroup, Long> {
}
