package project.coca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.coca.domain.personal.Member;
import project.coca.domain.request.GroupRequest;

import java.util.List;

public interface GroupRequestRepository extends JpaRepository<GroupRequest, Long> {
    List<GroupRequest> findByReceiverOrderByCreatedDateDesc(Member receiver);
}
