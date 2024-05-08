package project.coca.controller;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public boolean JoinReq(@RequestBody Member joinMember) {
        //프로필이미지url 없으면 걍 null로 해서 넘겨주면 되어요
        return memberService.memberJoin(joinMember);
    }

    @PostMapping("/loginReq")
    public boolean LoginReq(@RequestBody Member loginMember) {
        return memberService.login(loginMember);
    }

    @PostMapping("/withdrawalReq")
    public boolean WithdrawalReq(@RequestBody Member withdrawalMember) {
        return memberService.withdrawal(withdrawalMember);
    }

    @PostMapping("/memberInfoInquiryReq")
    public Member MemberInfoInquiryReq(@RequestBody Member inquiryMember) {
        return memberService.memberInfoInquiry(inquiryMember);
    }

    @PostMapping("/memberInfoUpdateReq")
    public Member MemberInfoUpdateReq(@RequestBody Member newInfo) {
        //프로필이미지url 없으면 걍 null로 해서 넘겨주면 되어요
        return memberService.memberInfoUpdate(newInfo);
    }
}
