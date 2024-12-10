package project.coca.v1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.coca.v1.domain.personal.Member;
import project.coca.v1.domain.request.FriendRequest;
import project.coca.v1.domain.request.RequestStatus;

import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    List<FriendRequest> findByReceiverOrderByCreatedDateDesc(Member receiver);

    Optional<FriendRequest> findBySenderAndReceiverAndRequestStatus(Member member, Member opponent, RequestStatus status);
}
