package project.coca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.coca.domain.group.GroupSchedule;

import java.time.LocalDateTime;
import java.util.List;

public interface GroupScheduleRepository extends JpaRepository<GroupSchedule, Long> {
    GroupSchedule save(GroupSchedule groupSchedule);

    @Query("select s from GroupSchedule s where (s.coGroup.id = :groupId and s.startTime >= :startDate) and s.endTime <= :endDate order by s.startTime asc")
    List<GroupSchedule> findGroupScheduleSummary(Long groupId, LocalDateTime startDate, LocalDateTime endDate);

}
