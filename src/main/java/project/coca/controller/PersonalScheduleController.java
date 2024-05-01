package project.coca.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import project.coca.domain.personal.PersonalSchedule;
import project.coca.service.PersonalScheduleService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/personal-schedule")
public class PersonalScheduleController {
    private final PersonalScheduleService personalScheduleService;

    /**
     * 개인 일정 등록
     *
     * @param personalSchedule
     * @return personalSchedule
     */
    @PostMapping("/add")
    private PersonalSchedule addPersonalSchedule(@RequestBody PersonalSchedule personalSchedule) {
        log.info("Add personal schedule: {}", personalSchedule);
        return personalScheduleService.savePersonalSchedule(personalSchedule);
    }

    /**
     * 개인 일정 목록 조회(by startDate, endDate)
     *
     * @param startDate 예시 : 2024-05-01
     * @param endDate   예시 : 2024-05-31
     * @return List<PersonalSchedule>
     */
    @GetMapping("/between-dates")
    public List<PersonalSchedule> getPersonalSchedulesByDates(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        log.info("Get personal schedules by dates: {}", startDate);
        log.info("Get personal schedules by dates: {}", endDate);
        return personalScheduleService.findPersonalSchedulesByDates(
                startDate,
                endDate);
    }

    /**
     * 개인 일정 상세 정보 조회 by id
     *
     * @param id
     * @return
     */
    @GetMapping("/id")
    private Optional<PersonalSchedule> getPersonalScheduleById(@RequestParam Long id) {
        log.info("Get personal schedule by id: {}", id);
        return personalScheduleService.findPersonalScheduleById(id);
    }
}
