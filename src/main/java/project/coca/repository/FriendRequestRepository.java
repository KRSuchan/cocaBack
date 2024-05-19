package project.coca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.coca.domain.personal.Member;
import project.coca.domain.request.FriendRequest;

import java.util.List;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    List<FriendRequest> findByReceiverOrderByCreatedDateDesc(Member receiver);
}
