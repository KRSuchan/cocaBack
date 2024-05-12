package project.coca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.coca.domain.tag.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag findByName(String name);
}
