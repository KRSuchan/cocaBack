package project.coca.v2.member;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/v2/member")
@AllArgsConstructor
public class MemberController {
    private MemberService memberService;
}
