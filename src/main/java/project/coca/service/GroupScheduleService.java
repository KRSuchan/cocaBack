package project.coca.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.coca.domain.group.*;
import project.coca.dto.request.GroupScheduleAttachmentRequest;
import project.coca.dto.request.GroupScheduleRequest;
import project.coca.repository.*;

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
    @Autowired
    private final GroupRepository groupRepository;

    //파일의 md5 생성
    private String generateFileMd5(File newFile) throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(Files.readAllBytes(newFile.toPath()));
        byte[] hash = md.digest();

        return new BigInteger(1, hash).toString(16);
    }

    private GroupScheduleAttachment generateAttachment(
            MultipartFile file, GroupSchedule schedule)
            throws NoSuchAlgorithmException, IOException
    {
        GroupScheduleAttachment changeAttach = new GroupScheduleAttachment();

        changeAttach.setFileName(file.getOriginalFilename());
        changeAttach.setFilePath("aws에 올리는 로직 추가 후 변경");
        changeAttach.setFileMd5(generateFileMd5(file.getResource().getFile()));
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

        return groupScheduleRepository.findGroupSchedule(groupId, startDate, endDate);
    }

    /* 그룹 일정 등록
    그룹이 존재하는지 확인 & 신청한 멤버가 관리자인지 확인 -> 등록
    파일 저장하는 로직 추가 필요
     */
    public GroupSchedule groupScheduleRegistrationReq(GroupScheduleRequest requestSchedule, MultipartFile[] files) throws NoSuchAlgorithmException, IOException
    {
        CoGroup group = groupRepository.findById(requestSchedule.getGroupId())
                .orElseThrow(() -> new NoSuchElementException("그룹이 존재하지 않습니다."));

        GroupManager checkUser = groupManagerRepository.checkUserIsManager(requestSchedule.getMemberId(), requestSchedule.getGroupId())
                .orElseThrow(() -> new NoSuchElementException("해당 그룹의 관리자가 아닙니다."));

        GroupSchedule registSchedule = new GroupSchedule();

        registSchedule.setCoGroup(group);
        registSchedule.setTitle(requestSchedule.getTitle());
        registSchedule.setDescription(requestSchedule.getDescription());
        registSchedule.setLocation(requestSchedule.getLocation());
        registSchedule.setStartTime(requestSchedule.getStartTime());
        registSchedule.setEndTime(requestSchedule.getEndTime());
        registSchedule.setColor(requestSchedule.getColor());

        List<GroupScheduleAttachment> attachments = new ArrayList<>();
        if(files != null || files.length > 0)
            for(MultipartFile file : files)
                attachments.add(generateAttachment(file, registSchedule));

        registSchedule.setGroupScheduleAttachments(attachments);

        return groupScheduleRepository.save(registSchedule);
    }

    /* 그룹 일정 수정
    일정이 존재하는지 확인 & 신청한 멤버가 관리자인지 확인 -> 등록
    파일 저장하는 로직 추가 필요
     */
    public GroupSchedule groupScheduleUpdate(GroupScheduleRequest requestSchedule, MultipartFile[] files, Long scheduleId)
            throws NoSuchAlgorithmException, IOException
    {
        GroupManager checkUser = groupManagerRepository.checkUserIsManager(requestSchedule.getMemberId(), requestSchedule.getGroupId())
                .orElseThrow(() -> new NoSuchElementException("해당 그룹의 관리자가 아닙니다."));

        GroupSchedule updateSchedule = groupScheduleRepository.findById(scheduleId)
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
        for(MultipartFile file : files)
            newAttachMD5s.add(generateFileMd5(file.getResource().getFile()));

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
                GroupScheduleAttachment newAttach = generateAttachment(files[i], updateSchedule);
                groupScheduleAttachmentRepository.save(newAttach);
                updateSchedule.addAttachment(newAttach);
            }
        }

        return groupScheduleRepository.save(updateSchedule);
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

        groupScheduleAttachmentRepository.deleteAll(deleteSchedule.getGroupScheduleAttachments());

        groupScheduleRepository.delete(deleteSchedule);
        groupScheduleRepository.flush();

        if(groupScheduleRepository.existsById(scheduleId))
            return false;
        else
            return true;
    }

}
