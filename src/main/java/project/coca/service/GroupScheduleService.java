package project.coca.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.coca.domain.group.GroupManager;
import project.coca.domain.group.GroupMember;
import project.coca.domain.group.GroupSchedule;
import project.coca.domain.group.GroupScheduleAttachment;
import project.coca.dto.request.GroupScheduleAttachmentRequest;
import project.coca.dto.request.GroupScheduleUpdateRequest;
import project.coca.dto.response.group.GroupScheduleResponse;
import project.coca.dto.response.group.GroupScheduleSummaryResponse;
import project.coca.repository.GroupManagerRepository;
import project.coca.repository.GroupMemberRepository;
import project.coca.repository.GroupScheduleAttachmentRepository;
import project.coca.repository.GroupScheduleRepository;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
    @Autowired
    private final GroupManagerRepository groupManagerRepository;
    @Autowired
    private final GroupScheduleAttachmentRepository groupScheduleAttachmentRepository;

    //파일의 md5 생성
    private String generateFileMd5(File newFile) throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(Files.readAllBytes(newFile.toPath()));
        byte[] hash = md.digest();

        return new BigInteger(1, hash).toString(16);
    }

    private GroupScheduleAttachment generateAttachment(
            GroupScheduleAttachmentRequest attach, GroupSchedule schedule)
            throws NoSuchAlgorithmException, IOException
    {
        GroupScheduleAttachment changeAttach = new GroupScheduleAttachment();

        changeAttach.setFileName(attach.getFileName());
        changeAttach.setFilePath("aws에 올리는 로직 추가 후 변경");
        changeAttach.setFileMd5(generateFileMd5(attach.getFile()));
        changeAttach.setGroupSchedule(schedule);

        return changeAttach;
    }

    /* 그룹 일정 목록 조회 & 그룹 일정 상세 정보 조회
    멤버가 그룹에 포함되어있는지 확인
        -> 그룹 일정 조회
        -> 일정 반환
    */
    public List<GroupSchedule> groupScheduleInquiry(
            Long groupId, String memberId, LocalDate startDay, LocalDate endDay)
    {
        GroupMember checkMember = groupMemberRepository.checkMemberInGroup(groupId, memberId)
                .orElseThrow(() -> new NoSuchElementException("회원이 그룹에 속해있지 않습니다."));

        LocalDateTime startDate = startDay.atStartOfDay();
        LocalDateTime endDate = endDay.atTime(LocalTime.of(23, 59, 59));

        return groupScheduleRepository.findGroupScheduleSummary(groupId, startDate, endDate);
    }

    public void groupScheduleUpdate(GroupScheduleUpdateRequest requestSchedule)
            throws NoSuchAlgorithmException, IOException
    {
        GroupManager checkUser = groupManagerRepository.checkUserIsManager(requestSchedule.getMemberId(), requestSchedule.getGroupId())
                .orElseThrow(() -> new NoSuchElementException("해당 그룹의 관리자가 아닙니다."));

        GroupSchedule updateSchedule = groupScheduleRepository.findById(requestSchedule.getScheduleId())
                .orElseThrow(() -> new NoSuchElementException("일정이 조회되지 않습니다."));

        updateSchedule.setTitle(requestSchedule.getTitle());
        updateSchedule.setDescription(requestSchedule.getDescription());
        updateSchedule.setLocation(requestSchedule.getLocation());
        updateSchedule.setStartTime(requestSchedule.getStartTime());
        updateSchedule.setEndTime(requestSchedule.getEndTime());
        updateSchedule.setColor(requestSchedule.getColor());

        List<String> existAttachMD5s = new ArrayList<>();
        for(GroupScheduleAttachment attachment : updateSchedule.getGroupScheduleAttachments())
            existAttachMD5s.add(attachment.getFileMd5());

        List<String> newAttachMD5s = new ArrayList<>();
        for(GroupScheduleAttachmentRequest attachment : requestSchedule.getAttachments())
            newAttachMD5s.add(generateFileMd5(attachment.getFile()));

        //복사해서 바꿔야 오류가 안납니다...
        List<GroupScheduleAttachment> attachmentsCopy = new ArrayList<>(updateSchedule.getGroupScheduleAttachments());

        //새로운거에 없음 -> 기존 DB에서 삭제
        for(GroupScheduleAttachment attachment : attachmentsCopy) {
            if(!newAttachMD5s.contains(attachment.getFileMd5())) {
                groupScheduleAttachmentRepository.delete(attachment);
                updateSchedule.removeAttachment(attachment);
            }
        }

        //기존거에 없음 -> 기존거에 새로운거 추가
        for(int i = 0; i < newAttachMD5s.size(); i++) {
            if(!existAttachMD5s.contains(newAttachMD5s.get(i))) {
                GroupScheduleAttachment newAttach = generateAttachment(requestSchedule.getAttachments().get(i), updateSchedule);
                groupScheduleAttachmentRepository.save(newAttach);
                updateSchedule.addAttachment(newAttach);
            }
        }

    }

    /* 그룹 일정 삭제
    해당 멤버가 관리자인지 확인
        -> 관리자라면 일정 조회
        -> 일정이 존재한다면 일정 삭제 -> 삭제 확인 후 true 반환. false는 일정 삭제 안된것.

        추후 aws에서 파일 삭제하는 로직도 추가해야함
    */
    public boolean groupScheduleDelete(Long groupId, Long scheduleId, String memberId) {
        GroupManager checkUser = groupManagerRepository.checkUserIsManager(memberId, groupId)
                .orElseThrow(() -> new NoSuchElementException("해당 그룹의 관리자가 아닙니다."));

        GroupSchedule deleteSchedule = groupScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new NoSuchElementException("일정이 조회되지 않습니다."));


        groupScheduleRepository.delete(deleteSchedule);
        groupScheduleRepository.flush();

        if(groupScheduleRepository.existsById(scheduleId))
            return false;
        else
            return true;
    }

}
