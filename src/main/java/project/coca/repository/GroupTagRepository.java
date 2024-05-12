package project.coca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.coca.domain.tag.GroupTag;

public interface GroupTagRepository extends JpaRepository<GroupTag, Long> {
    void deleteAllByCoGroupId(Long groupId);
}
