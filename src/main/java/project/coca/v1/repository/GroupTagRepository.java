package project.coca.v1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.coca.v1.domain.tag.GroupTag;

public interface GroupTagRepository extends JpaRepository<GroupTag, Long> {
    void deleteAllByCoGroupId(Long groupId);
}
