package project.coca.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.coca.domain.group.CoGroup;
import project.coca.domain.personal.Member;
import project.coca.repository.GroupRepository;
import project.coca.repository.MemberRepository;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;
    private final MemberRepository memberRepository;

    /**
     * 21. 그룹 등록
     */
    public CoGroup saveGroup(Member creator, CoGroup group) {
        Member admin = memberRepository.findById(creator.getId())
                .orElseThrow(() -> new NoSuchElementException("회원이 조회되지 않습니다."));
        // 그룹에 admin 회원 반영
        group.setAdmin(admin);
        return groupRepository.save(group);
    }

    /**
     * 22-1. 그룹 검색 by 검색어
     */
//    public List<CoGroup> findGroupsByTitle(String title) {
//
//    }
}
