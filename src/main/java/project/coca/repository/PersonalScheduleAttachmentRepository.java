package project.coca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.coca.domain.personal.PersonalScheduleAttachment;

public interface PersonalScheduleAttachmentRepository extends JpaRepository<PersonalScheduleAttachment, Long> {

}
