package project.coca.v2.member;

import org.springframework.data.jpa.repository.JpaRepository;
import project.coca.domain.personal.Member;

public interface MemberRepository extends JpaRepository<Member, String> {
}
