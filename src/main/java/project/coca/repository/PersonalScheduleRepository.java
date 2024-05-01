package project.coca.repository;

import org.springframework.data.repository.CrudRepository;
import project.coca.domain.personal.PersonalSchedule;

import java.time.LocalDateTime;
import java.util.List;

public interface PersonalScheduleRepository extends CrudRepository<PersonalSchedule, Long> {
    /**
     * 개인 일정 목록 조회 : start와 end 사이에 속한 개인 일정 목록 조회
     * TODO :
     *    "startTime": "2024-04-01 00:00:00",
     *     "endTime": "2024-06-10 23:59:59",
     *     인 데이터가 5월 탐색 시도 시 탐색되지 않음
     *
     * @param start1
     * @param end1
     * @param start2
     * @param end2
     * @return
     */
    List<PersonalSchedule> findByStartTimeBetweenOrEndTimeBetween(LocalDateTime start1, LocalDateTime end1, LocalDateTime start2, LocalDateTime end2);
}
