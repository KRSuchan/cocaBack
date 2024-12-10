package project.coca.v1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.coca.v1.domain.group.GroupNotice;

public interface GroupNoticeRepository extends JpaRepository<GroupNotice, Long> {

    void deleteByCoGroupId(Long groupId);

    GroupNotice findByCoGroupId(Long id);
}
