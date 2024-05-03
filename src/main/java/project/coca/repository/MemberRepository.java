package project.coca.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.coca.domain.personal.Member;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {
    Member save(Member member);
    Optional<Member> findById(String id);
    boolean existsById(String id); //id와 동일한 회원 존재하는지 확인
    void delete(Member member);
//    Member updateById(String)
}
