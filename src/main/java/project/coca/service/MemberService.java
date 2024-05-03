package project.coca.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.coca.repository.MemberRepository;
import project.coca.domain.personal.Member;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class MemberService {
    @Autowired
    private final MemberRepository memberRepository;
    private String memberId; //개발 위해 임시로 아이디 저장. 인증 시스템 개발 후 수정하기.

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    //비밀번호 확인 메소드
    public boolean passwordCheck(Member member) {
        Optional<Member> check = memberRepository.findById(member.getId());

        if(member.getPassword().equals(check.get().getPassword()))
            return true;

        return false;
    }

    //회원가입
    public boolean memberJoin(Member member) {
        boolean isExistSameMember = memberRepository.existsById(member.getId());

        if(!isExistSameMember) {
            Member check = memberRepository.save(member);
            memberRepository.flush();

            if(check.getId() == member.getId()) {
                return true;
            }
        }
        return false;
    }

    //로그인
    public boolean login(Member loginMember) {
        Optional<Member> checkMember = memberRepository.findById(loginMember.getId());

        if(checkMember.isPresent()) {
            if(loginMember.getPassword().equals(checkMember.get().getPassword())) {
                setMemberId(loginMember.getId()); //낸주 삭제
                return true;
            }
        }
        return false; //위 상황 아니면 id or 비번 틀림
    }

    //회원탈퇴
    public boolean withdrawal(Member withdrawalMember) {
        if(passwordCheck(withdrawalMember)) {
            memberRepository.delete(withdrawalMember);
            memberRepository.flush();

            if(!memberRepository.existsById(withdrawalMember.getId()))
                return true;
        }
        return false;
    }

    //개인정보조회
    public Member memberInfoInquiry(Member member) {
        Member inquiryMember = null;
        if(passwordCheck(member)) {
            inquiryMember = memberRepository.findById(member.getId()).get();
        }
        return inquiryMember;
    }

    //개인정보수정 -> 개인정보조회가 선행이라 pw 확인x
    public Member memberInfoUpdate(Member newInfo) {
        Member member = memberRepository.findById(newInfo.getId()).get();

        member.setPassword(newInfo.getPassword());
        member.setUserName(newInfo.getUserName());
        member.setProfileImgPath(newInfo.getProfileImgPath());

        Member check = memberRepository.save(member);
        memberRepository.flush();

        if(check.getId() == member.getId()) {
            return check;
        }
        return null;
    }
}
