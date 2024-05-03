package project.coca.controller;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.coca.domain.personal.Member;
import project.coca.service.MemberService;

@ComponentScan
@RequestMapping("/api/member")
@RestController
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    //프로필 이미지는 필수 아님
    @PostMapping("/joinReq")
    public boolean JoinReq(@RequestParam String id, @RequestParam String pw, @RequestParam String userName, @RequestParam(required = false) String profileImgPath) {
        Member joinMember;

        if(profileImgPath == null) {
            joinMember = new Member(id, pw, userName);
        }
        else {
            joinMember = new Member(id, pw, userName, profileImgPath);
        }

        return memberService.memberJoin(joinMember);
    }

    @PostMapping("/loginReq")
    public boolean LoginReq(@RequestParam String id, @RequestParam String pw) {
        Member loginMember = new Member(id, pw);
        return memberService.login(loginMember);
    }

    @PostMapping("/withdrawalReq")
    public boolean WithdrawalReq(@RequestParam String id, @RequestParam String pw) {
        Member withdrawalMember = new Member(id, pw);
        return memberService.withdrawal(withdrawalMember);
    }

    @PostMapping("/memberInfoInquiryReq")
    public Member MemberInfoInquiryReq(@RequestParam String id, @RequestParam String pw) {
        Member inquiryMember = new Member(id, pw);
        return memberService.memberInfoInquiry(inquiryMember);
    }

    @PostMapping("/memberInfoUpdateReq")
    public Member MemberInfoUpdateReq(@RequestParam String id, @RequestParam String pw, @RequestParam String userName, @RequestParam(required = false) String profileImgPath) {
        Member newInfo;

        if(profileImgPath == null) {
            newInfo = new Member(id, pw, userName);
        }
        else {
            newInfo = new Member(id, pw, userName, profileImgPath);
        }
        return memberService.memberInfoUpdate(newInfo);
    }
}
