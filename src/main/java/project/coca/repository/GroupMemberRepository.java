package project.coca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.coca.domain.group.GroupMember;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    @Query("select m from GroupMember m where m.coGroup.id = :groupId and m.groupMember.id = :memberId")
    Optional<GroupMember> checkMemberInGroup(Long groupId, String memberId);
}
