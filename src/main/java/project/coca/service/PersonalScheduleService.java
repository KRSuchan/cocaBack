package project.coca.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.coca.domain.personal.Member;
import project.coca.domain.personal.PersonalSchedule;
import project.coca.domain.personal.PersonalScheduleAttachment;
import project.coca.repository.MemberRepository;
import project.coca.repository.PersonalScheduleRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class PersonalScheduleService {
    private final PersonalScheduleRepository personalScheduleRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public PersonalScheduleService(PersonalScheduleRepository personalScheduleRepository, MemberRepository memberRepository) {
        this.personalScheduleRepository = personalScheduleRepository;
        this.memberRepository = memberRepository;
    }

    /**
     * 09. 개인 일정 등록
     *
     * @param personalSchedule : 작성한 개인 일정 id를 제외한 전체
     * @return : 저장된 개인 일정 return
     */
    public PersonalSchedule savePersonalSchedule(Member member,
                                                 PersonalSchedule personalSchedule,
                                                 List<PersonalScheduleAttachment> attachments) {
        Member foundMember = memberRepository.findById(member.getId())
                .orElseThrow(() -> new NoSuchElementException("회원이 조회되지 않습니다."));

        // 일정에 회원 반영
        personalSchedule.setMember(foundMember);

        PersonalSchedule savedSchedule = personalScheduleRepository.save(personalSchedule);

        // 첨부파일이 있는 경우
        if (attachments != null && !attachments.isEmpty()) {
            for (PersonalScheduleAttachment attachment : attachments) {
                attachment.setPersonalSchedule(savedSchedule);
            }
            savedSchedule.getAttachments().addAll(attachments);
        }

        return savedSchedule;
    }

    /**
     * 10. 개인 일정 목록 조회 (요약 정보)
     * 11. 개인 일정 상세 정보 조회 (목록으로 반환)
     *
     * @param memberId 회원 계정 id
     * @param start    기간 시작
     * @param end      기간 끝
     * @return List<PersonalSchedule>
     */
    public List<PersonalSchedule> findPersonalSchedulesByDates(String memberId, LocalDate start, LocalDate end) {
        Member member =
                memberRepository.findById(memberId).orElseThrow(() -> new NoSuchElementException("회원이 조회되지 않습니다."));

        // LocalDate 에서 LocalDateTime 변환
        LocalDateTime startDT = start.atStartOfDay();
        LocalDateTime endDT = end.atTime(LocalTime.of(23, 59, 59));
        // 기간 일정 목록 조회
        return personalScheduleRepository.findPersonalScheduleByDateRange(memberId, startDT, endDT);
    }


    /**
     * 12. 개인 일정 수정
     */
    public PersonalSchedule updatePersonalSchedule(Member member,
                                                   PersonalSchedule updatedPersonalSchedule,
                                                   List<PersonalScheduleAttachment> attachments) {
        Member foundMember = memberRepository.findById(member.getId())
                .orElseThrow(() -> new NoSuchElementException("회원이 조회되지 않습니다."));

        PersonalSchedule foundPersonalSchedule = personalScheduleRepository.findById(updatedPersonalSchedule.getId())
                .orElseThrow(() -> new NoSuchElementException("일정이 조회되지 않습니다."));
        System.out.println("회원 조회, 일정 조회 완료");

        // 수정된 내용 반영
        foundPersonalSchedule.setTitle(updatedPersonalSchedule.getTitle());
        foundPersonalSchedule.setDescription(updatedPersonalSchedule.getDescription());
        foundPersonalSchedule.setLocation(updatedPersonalSchedule.getLocation());
        foundPersonalSchedule.setStartTime(updatedPersonalSchedule.getStartTime());
        foundPersonalSchedule.setEndTime(updatedPersonalSchedule.getEndTime());
        foundPersonalSchedule.setColor(updatedPersonalSchedule.getColor());
        foundPersonalSchedule.setIsPrivate(updatedPersonalSchedule.getIsPrivate());

        System.out.println("수정된 내용 반영 완료");

        // 첨부파일이 있는 경우
        if (attachments != null && !attachments.isEmpty()) {
            for (PersonalScheduleAttachment attachment : attachments) {
                attachment.setPersonalSchedule(foundPersonalSchedule);
            }
            foundPersonalSchedule.getAttachments().addAll(attachments);
        }

        System.out.println("첨부파일 반영 완료");

        // 수정된 개인 일정 저장
        return personalScheduleRepository.save(foundPersonalSchedule);
    }

    /**
     * 13. 개인 일정 삭제
     */
    public void deletePersonalScheduleById(String memberId, Long personalScheduleId) {
        Member foundMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("회원이 조회되지 않습니다."));

        PersonalSchedule foundPersonalSchedule = personalScheduleRepository.findById(personalScheduleId)
                .orElseThrow(() -> new NoSuchElementException("일정이 조회되지 않았습니다."));

        // 일정 삭제 수행
        personalScheduleRepository.deleteById(personalScheduleId);
    }

}
