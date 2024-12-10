package project.coca.v1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.coca.v1.domain.tag.Interest;

public interface InterestRepository extends JpaRepository<Interest, Long> {
    void deleteById(Long id);
}
