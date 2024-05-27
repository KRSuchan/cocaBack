package project.coca.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import project.coca.domain.tag.Interest;
import project.coca.domain.tag.Tag;
import project.coca.dto.request.MemberFunctionRequest;
import project.coca.dto.request.MemberRequest;
import project.coca.dto.response.tag.InterestForTag;
import project.coca.repository.InterestRepository;
import project.coca.repository.MemberRepository;
import project.coca.domain.personal.Member;
import project.coca.repository.TagRepository;

import javax.naming.AuthenticationException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


@Service
@RequiredArgsConstructor
public class MemberService {
    @Autowired
    private final MemberRepository memberRepository;
    @Autowired
    private final TagRepository tagRepository;
    @Autowired
    private final InterestRepository interestRepository;

    //관심사 세팅~
    private List<Interest> setInterest(List<InterestForTag> interestId, Member member) {
        List<Interest> memberInterest = new ArrayList<>();

        if (interestId != null && interestId.size() > 0) {
            for (InterestForTag interest : interestId) {
                Tag tag = tagRepository.findById(interest.getTagId())
                        .orElseThrow(() -> new NoSuchElementException("존재하지 않는 관심사입니다."));

                memberInterest.add(new Interest(member, tag));
            }
        }
        return memberInterest;
    }

    //회원가입
    public Member memberJoin(MemberRequest joinMember) {
        if(memberRepository.existsById(joinMember.getId()))
            throw new DuplicateKeyException("동일한 아이디의 회원이 이미 존재합니다.");

        Member member = new Member(joinMember.getId(), joinMember.getPassword(),
                joinMember.getUserName(), joinMember.getProfileImgPath());

        //관심사 선택했다면 관심사도 등록 (등록 전에 관심사 있는지 검사)
        List<Interest> memberInterest;
        try {
            memberInterest = setInterest(joinMember.getInterestId(), member);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("존재하지 않는 관심사입니다.");
        }
        //새 관심사 세팅
        member.setInterests(memberInterest);

        Member join = memberRepository.save(member);
        memberRepository.flush();

        return join;
    }

    //로그인
    public boolean login(MemberFunctionRequest loginMember) {
        Member check = memberRepository.findById(loginMember.getId())
                .orElseThrow(() -> new NoSuchElementException("아이디 혹은 비밀번호가 일치하지 않습니다."));

        return loginMember.getPassword().equals(check.getPassword());
    }

    //회원탈퇴
    public boolean withdrawal(MemberFunctionRequest withdrawalMember) throws AuthenticationException {
        Member check = memberRepository.findById(withdrawalMember.getId())
                .orElseThrow(() -> new NoSuchElementException("회원이 조회되지 않습니다."));

        if(withdrawalMember.getPassword().equals(check.getPassword())) {
            memberRepository.delete(check);
            memberRepository.flush();

            //existsById -> 존재하면 true 반환, 존재하지 않으면 false 반환
            //존재하지 않으면 성공이니까 !existsById return.
           return !memberRepository.existsById(withdrawalMember.getId());
        }
        else
            throw new AuthenticationException("비밀번호가 일치하지 않습니다.");
    }

    //개인정보조회
    public Member memberInfoInquiry(MemberFunctionRequest member) throws AuthenticationException {
        Member inquiryMember = memberRepository.findById(member.getId())
                .orElseThrow(() -> new NoSuchElementException("회원이 조회되지 않습니다."));

        if(member.getPassword().equals(inquiryMember.getPassword()))
            return inquiryMember;
        else
            throw new AuthenticationException("비밀번호가 일치하지 않습니다.");
    }

    //개인정보수정 -> 개인정보조회가 선행이라 pw 확인x
    public Member memberInfoUpdate(MemberRequest newInfo) {
        Member member = memberRepository.findById(newInfo.getId())
                .orElseThrow(() -> new NoSuchElementException("회원이 조회되지 않습니다."));

        member.setPassword(newInfo.getPassword());
        member.setUserName(newInfo.getUserName());
        member.setProfileImgPath(newInfo.getProfileImgPath());

        //새 관심사 넣기 전 기존 관심사 삭제
        if(member.getInterests() != null && member.getInterests().size() > 0)
            for(Interest interest : member.getInterests())
                interestRepository.delete(interest);

        List<Interest> memberInterest;
        try {
            memberInterest = setInterest(newInfo.getInterestId(), member);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("존재하지 않는 관심사입니다.");
        }
        //새 관심사 세팅
        member.setInterests(memberInterest);

        Member check = memberRepository.save(member);
        memberRepository.flush();

        return check;
    }
}
