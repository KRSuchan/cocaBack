package project.coca.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.ValidationFailureException;
import org.springframework.stereotype.Service;
import project.coca.domain.group.CoGroup;
import project.coca.domain.group.GroupManager;
import project.coca.domain.group.GroupMember;
import project.coca.domain.personal.Member;
import project.coca.domain.tag.Tag;
import project.coca.repository.GroupManagerRepository;
import project.coca.repository.GroupMemberRepository;
import project.coca.repository.GroupRepository;
import project.coca.repository.MemberRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class GroupService {
    private final GroupRepository groupRepository;
    private final MemberRepository memberRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final GroupManagerRepository groupManagerRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository, MemberRepository memberRepository, GroupMemberRepository groupMemberRepository, GroupManagerRepository groupManagerRepository) {
        this.groupRepository = groupRepository;
        this.memberRepository = memberRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.groupManagerRepository = groupManagerRepository;
    }

    /**
     * 07. 캘린더 목록 조회
     */
    public List<CoGroup> findJoinedGroups(String memberId) {
        // 1. 회원 검증
        memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("회원이 조회되지 않습니다."));
        // 2. 회원이 참가중인 그룹 목록 조회
        return groupRepository.findByGroupMemberId(memberId);
    }

    /**
     * alpha. 그룹 참가하기
     */
    public void joinGroup(String memberId, Long groupId) {
        // 1. 회원 검증
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("회원이 조회되지 않습니다."));
        // 2. 그룹 조회
        CoGroup group = groupRepository.findById(groupId)
                .orElseThrow(() -> new NoSuchElementException("그룹이 조회되지 않습니다."));
        // 3. 회원 그룹 데이터를 groupMember에 넣기
        GroupMember groupMember = new GroupMember();
        groupMember.setGroupMember(member);
        groupMember.setCoGroup(group);
        // 4. 그룹 회원 등록
        groupMemberRepository.save(groupMember);
    }

    /**
     * alpha. 그룹에서 나가기
     */
    public void leaveGroup(String memberId, Long groupId) {
        // 1. 회원 검증
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("회원이 조회되지 않습니다."));
        // 2. 그룹 조회
        CoGroup group = groupRepository.findById(groupId)
                .orElseThrow(() -> new NoSuchElementException("그룹이 조회되지 않습니다."));
        // 3. 그룹 멤버 삭제 수행 (그룹에서 나가기)
        groupMemberRepository.deleteGroupMemberByCoGroupAndGroupMember(group, member);
    }

    /**
     * 21. 그룹 등록
     */
    public CoGroup saveGroup(Member creator, CoGroup group) {
        // 회원 검증
        Member admin = memberRepository.findById(creator.getId())
                .orElseThrow(() -> new NoSuchElementException("회원이 조회되지 않습니다."));

        // 그룹에 admin 회원 반영하고 그룹 생성
        group.setAdmin(admin);
        CoGroup savedGroup = groupRepository.save(group);
        // 관리자를 그룹 멤버에 등록하기
        GroupMember groupMember = new GroupMember();
        groupMember.setCoGroup(savedGroup);
        groupMember.setGroupMember(admin);
        groupMemberRepository.save(groupMember);
        // 관리자를 그룹 매니저에 등록하기
        GroupManager groupManager = new GroupManager();
        groupManager.setCoGroup(savedGroup);
        groupManager.setGroupManager(admin);
        groupManagerRepository.save(groupManager);

        // 최종 리턴
        return savedGroup;
    }

    /**
     * 22-1. 그룹 검색 by 검색어(제목)
     */
    public List<CoGroup> findGroupsByNameLike(String groupName) {
        return groupRepository.findByNameContaining(groupName);
    }

    /**
     * 22-2. 그룹 검색 by 태그
     */
    public List<CoGroup> findGroupsByTag(Tag tag) {
        return groupRepository.findByTagName(tag);
    }

    /**
     * 23. 그룹 상세 정보 조회
     */
    public CoGroup findGroupById(Long groupId) {
        CoGroup group = groupRepository.findById(groupId)
                .orElseThrow(() -> new NoSuchElementException("그룹이 조회되지 않습니다."));
        return group;
    }

    /**
     * 25. 그룹 수정
     */
    public void updateGroup(Long groupId, String adminId, CoGroup updateGroup) {
        CoGroup findGroup = groupRepository.findById(groupId)
                .orElseThrow(() -> new NoSuchElementException("그룹이 조회되지 않습니다."));
        if (findGroup.getAdmin().equals(adminId)) {
            findGroup.setName(updateGroup.getName());
            findGroup.setDescription(updateGroup.getDescription());
            findGroup.setGroupManager(updateGroup.getGroupManager());
            groupRepository.save(findGroup);
        } else {
            throw new ValidationFailureException("수정 권한이 없습니다.");
        }
    }

    /**
     * 26. 그룹 삭제
     */
    public void deleteGroup(String adminId, Long groupId) {
        CoGroup findGroup = groupRepository.findById(groupId)
                .orElseThrow(() -> new NoSuchElementException("그룹이 조회되지 않습니다."));
        if (findGroup.getAdmin().equals(adminId)) {
            groupRepository.deleteById(findGroup.getId());
        } else {
            throw new ValidationFailureException("삭제 권한이 없습니다.");
        }
    }
}
