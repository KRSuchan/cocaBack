package project.coca.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.coca.domain.group.GroupMember;
import project.coca.domain.group.GroupSchedule;
import project.coca.dto.response.group.GroupScheduleResponse;
import project.coca.dto.response.group.GroupScheduleSummaryResponse;
import project.coca.repository.GroupMemberRepository;
import project.coca.repository.GroupScheduleRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class GroupScheduleService {
    @Autowired
    private final GroupScheduleRepository groupScheduleRepository;
    @Autowired
    private final GroupMemberRepository groupMemberRepository;

    //멤버가 그룹에 포함되어있는지 확인하는 메소드
    public boolean isExistMemberInGroup(Long groupId, String memberId) {
        Optional<GroupMember> checkMember = groupMemberRepository.checkMemberInGroup(groupId, memberId);

        if(checkMember.isPresent())
            return true;
        else
            return false;
    }

    /* 그룹 일정 목록 조회 & 그룹 일정 상세 정보 조회
    멤버가 그룹에 포함되어있는지 확인
        -> 그룹 일정 조회
        -> GroupScheduleSummaryResponse 형식으로 변환 후 반환
    */
    public List<GroupSchedule> groupScheduleInquiry(
            Long groupId, String memberId, LocalDate startDay, LocalDate endDay)
    {
        if(isExistMemberInGroup(groupId, memberId)) {
            LocalDateTime startDate = startDay.atStartOfDay();
            LocalDateTime endDate = endDay.atTime(LocalTime.of(23, 59, 59));

            return groupScheduleRepository.findGroupScheduleSummary(groupId, startDate, endDate);

//            groupScheduleList = groupScheduleRepository.findGroupScheduleSummary(groupId, startDate, endDate)
//                    .stream()
//                    .map(GroupScheduleSummaryResponse::of)
//                    .collect(Collectors.toList());


//            groupScheduleList = groupScheduleRepository.findGroupScheduleSummary(groupId, startDate, endDate)
//                    .stream()
//                    .map(GroupScheduleResponse::of)
//                    .collect(Collectors.toList());
        }
        else
            throw new NoSuchElementException(String.format("회원이 그룹에 속해있지 않습니다."));
    }

}
