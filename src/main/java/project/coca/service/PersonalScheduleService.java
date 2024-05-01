package project.coca.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.coca.domain.personal.PersonalSchedule;
import project.coca.repository.PersonalScheduleRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PersonalScheduleService {
    private final PersonalScheduleRepository personalScheduleRepository;

    /**
     * 개인 일정 등록
     *
     * @param personalSchedule : 작성한 개인 일정 id를 제외한 전체
     * @return : 저장된 개인 일정 return
     */
    public PersonalSchedule savePersonalSchedule(PersonalSchedule personalSchedule) {
        /*
         * TODO Member의 검증 필요
         */
        return personalScheduleRepository.save(personalSchedule);
    }

    /**
     * 개인 일정 목록 조회 (시작시간 ~ 끝나는시간)
     *
     * @param start : 기간 시작
     * @param end   : 기간 끝
     * @return List<PersonalSchedule>
     */
    public List<PersonalSchedule> findPersonalSchedulesByDates(LocalDate start, LocalDate end) {
        /*
         * TODO Member의 검증 필요
         */
        LocalDateTime startDT = start.atStartOfDay();
        LocalDateTime endDT = end.atTime(LocalTime.MAX);
        return personalScheduleRepository.findByStartTimeBetweenOrEndTimeBetween(startDT, endDT, startDT, endDT);
    }

    /**
     * 개인 일정 상세 정보 조회
     *
     * @param id : 개인 일정 id
     * @return : 조회된 개인 일정
     */
    public Optional<PersonalSchedule> findPersonalScheduleById(Long id) {
        return personalScheduleRepository.findById(id);
    }
}
