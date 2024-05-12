package project.coca.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.oxm.ValidationFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.coca.domain.group.CoGroup;
import project.coca.domain.group.GroupManager;
import project.coca.domain.group.GroupMember;
import project.coca.domain.personal.Member;
import project.coca.domain.tag.GroupTag;
import project.coca.domain.tag.Tag;
import project.coca.repository.*;

import javax.management.InstanceAlreadyExistsException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class GroupService {
    private final GroupRepository groupRepository;
    private final MemberRepository memberRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final GroupManagerRepository groupManagerRepository;
    private final TagRepository tagRepository;
    private final GroupTagRepository groupTagRepository;

    private Integer pageSize = 20;

    @Autowired
    public GroupService(GroupRepository groupRepository,
                        MemberRepository memberRepository,
                        GroupMemberRepository groupMemberRepository,
                        GroupManagerRepository groupManagerRepository,
                        TagRepository tagRepository, GroupTagRepository groupTagRepository) {
        this.groupRepository = groupRepository;
        this.memberRepository = memberRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.groupManagerRepository = groupManagerRepository;
        this.tagRepository = tagRepository;
        this.groupTagRepository = groupTagRepository;
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
     * a1. 그룹 참가하기
     */
    public void joinGroup(Member member, CoGroup group) throws InstanceAlreadyExistsException {
        // 1. 회원 검증
        Member findMember = memberRepository.findById(member.getId())
                .orElseThrow(() -> new NoSuchElementException("회원이 조회되지 않습니다."));
        // 2. 그룹 조회
        CoGroup findGroup = groupRepository.findById(group.getId())
                .orElseThrow(() -> new NoSuchElementException("그룹이 조회되지 않습니다."));
        // 3. 이미 참가중인가
        List<CoGroup> findJoinedGroups = groupRepository.findByGroupMemberId(findMember.getId());
        if (findJoinedGroups.contains(findGroup))
            throw new InstanceAlreadyExistsException("이미 참가중입니다.");
        // 그룹 패스워드 일치 여부
        if (findGroup.getPrivatePassword() == null
                || findGroup.getPrivatePassword().equals(group.getPrivatePassword())) {
            // 3. 회원 그룹 데이터를 groupMember에 넣기
            GroupMember groupMember = new GroupMember();
            groupMember.setGroupMember(findMember);
            groupMember.setCoGroup(findGroup);
            // 4. 그룹 회원 등록
            groupMemberRepository.save(groupMember);
        } else {
            throw new ValidationFailureException("비밀번호가 잘못되었습니다.");
        }

    }

    /**
     * a2. 그룹에서 탈퇴
     */
    public void leaveGroup(String memberId, Long groupId) {
        // 1. 회원 검증
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("회원이 조회되지 않습니다."));
        // 2. 그룹 조회
        CoGroup group = groupRepository.findById(groupId)
                .orElseThrow(() -> new NoSuchElementException("그룹이 조회되지 않습니다."));
        if (group.getAdmin().equals(member)) {
            // 그룹 관리자는 그룹 탈퇴 불가
            throw new ValidationFailureException("그룹 관리자는 탈퇴할 수 없습니다.");
        }
        if (group.getGroupManager().contains(member)) {
            // 그룹 매니저는 매니저 권한 반납 후 탈퇴
            groupManagerRepository.deleteByManagerIdAndGroupId(member.getId(), group.getId());
        }
        // 3. 그룹 멤버 삭제 수행 (그룹에서 탈퇴)
        groupMemberRepository.deleteGroupMemberByCoGroupAndGroupMember(group, member);
    }

    /**
     * 21. 그룹 등록
     */
    public CoGroup saveGroup(Member creator, CoGroup group, List<GroupTag> groupTags) {
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

        // 그룹 태그가 있는 경우
        if (groupTags != null && !groupTags.isEmpty()) {
            for (GroupTag groupTag : groupTags) {
                groupTag.setCoGroup(savedGroup);
                Tag tag = tagRepository.findById(groupTag.getTag().getId()).get();
                groupTag.setTag(tag);
            }
            savedGroup.setGroupTags(groupTags);
        }
        // 최종 리턴
        return groupRepository.findById(savedGroup.getId()).orElseThrow();
    }

    /**
     * 22-1. 그룹 검색 by 그룹명
     */
    public List<CoGroup> findGroupsByNameLike(String groupName, Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<CoGroup> resultPage = groupRepository
                .findByNameContainingOrderByGroupMembersDesc(groupName, pageable);
        return resultPage.getContent();
    }

    /**
     * 22-2. 그룹 검색 by 태그
     */
    public List<CoGroup> findGroupsByTag(String tagName, Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Tag tag = tagRepository.findByName(tagName);
        Page<CoGroup> resultPage = groupRepository
                .findByGroupTagsTagNameOrderByGroupMembersDesc(tag.getName(), pageable);
        return resultPage.getContent();
    }

    /**
     * 23. 그룹 상세 정보 조회
     */
    public CoGroup findGroupById(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new NoSuchElementException("그룹이 조회되지 않습니다."));
    }

    /**
     * 25. 그룹 수정
     */
    public void updateGroup(Long groupId, String adminId, CoGroup updateGroup, List<GroupTag> groupTags) {
        CoGroup findGroup = groupRepository.findById(groupId)
                .orElseThrow(() -> new NoSuchElementException("그룹이 조회되지 않습니다."));
        if (findGroup.getAdmin().getId().equals(adminId)) {
            findGroup.setName(updateGroup.getName());
            findGroup.setDescription(updateGroup.getDescription());
            findGroup.setGroupManager(updateGroup.getGroupManager());
            groupRepository.save(findGroup);
            groupTagRepository.deleteAllByCoGroupId(findGroup.getId());
            for (GroupTag groupTag : groupTags) {
                groupTag.setCoGroup(findGroup);
                Tag tag = tagRepository.findById(groupTag.getTag().getId()).get();
                groupTag.setTag(tag);
            }
            findGroup.setGroupTags(groupTags);
        } else {
            throw new ValidationFailureException("수정 권한이 없습니다.");
        }
    }

    /**
     * 25-a. Admin을 위한 그룹 상세 정보 조회
     */
    public CoGroup findGroupForAdmin(CoGroup group, Member admin) {
        Member findMember = memberRepository.findById(admin.getId())
                .orElseThrow(() -> new NoSuchElementException("회원이 조회되지 않습니다."));
        CoGroup findGroup = findGroupById(group.getId());
        if (!findMember.getPassword().equals(admin.getPassword())) {
            // 회원 pwd 불일치
            throw new NoSuchElementException("회원이 조회되지 않습니다.");
        } else if (!findGroup.getAdmin().getId().equals(admin.getId())) {
            // admin id 불일치
            throw new ValidationFailureException("조회 권한이 없습니다.");
        } else {
            return findGroup;
        }
    }

    /**
     * 26. 그룹 삭제
     */
    public void deleteGroup(String adminId, Long groupId) {
        CoGroup findGroup = groupRepository.findById(groupId)
                .orElseThrow(() -> new NoSuchElementException("그룹이 조회되지 않습니다."));
        if (findGroup.getAdmin().getId().equals(adminId)) {
            groupRepository.deleteById(findGroup.getId());
        } else {
            throw new ValidationFailureException("삭제 권한이 없습니다.");
        }
    }
}
