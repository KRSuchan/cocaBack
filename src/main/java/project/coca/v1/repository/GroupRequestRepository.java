package project.coca.v1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.coca.v1.domain.group.CoGroup;
import project.coca.v1.domain.personal.Member;
import project.coca.v1.domain.request.GroupRequest;
import project.coca.v1.domain.request.RequestStatus;

import java.util.List;
import java.util.Optional;

public interface GroupRequestRepository extends JpaRepository<GroupRequest, Long> {
    List<GroupRequest> findByReceiverOrderByCreatedDateDesc(Member receiver);

    Optional<GroupRequest> findByCoGroupAndReceiverAndRequestStatus(CoGroup group, Member receiver, RequestStatus requestStatus);
}
