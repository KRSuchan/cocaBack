package project.coca.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.coca.domain.group.CoGroup;

import java.util.List;

public interface GroupRepository extends JpaRepository<CoGroup, Long> {
    // 그룹 멤버 수를 기준으로 오름차순으로 페이징 및 정렬된 결과를 반환하는 메서드
    Page<CoGroup> findByNameContainingOrderByGroupMembersDesc(String name, Pageable pageable);

    @Query("SELECT g FROM CoGroup g JOIN g.groupMembers gm WHERE gm.groupMember.id = :groupMemberId")
    List<CoGroup> findByGroupMemberId(String groupMemberId);

    Page<CoGroup> findByGroupTagsTagNameOrderByGroupMembersDesc(String tagName, Pageable pageable);
}
