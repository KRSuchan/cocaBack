package project.coca.controller;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.coca.domain.personal.Member;
import project.coca.dto.request.MemberFunctionRequest;
import project.coca.dto.request.MemberRequest;
import project.coca.dto.response.Member.MemberResponse;
import project.coca.dto.response.common.ApiResponse;
import project.coca.dto.response.common.error.ErrorCode;
import project.coca.dto.response.common.success.ResponseCode;
import project.coca.service.MemberService;

import javax.naming.AuthenticationException;
import java.util.NoSuchElementException;

@ComponentScan
@RequestMapping("/api/member")
@RestController
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    //프로필 이미지는 필수 아님

    /**
     * 회원가입
     * @body joinMember 회원가입 할 회원의 정보
     * @return ApiResponse
     */
    @PostMapping("/joinReq")
    public ApiResponse<MemberResponse> JoinReq(@RequestBody MemberRequest joinMember) {
        try {
            MemberResponse joinResult = MemberResponse.of(memberService.memberJoin(joinMember));

            return ApiResponse.response(ResponseCode.OK, joinResult);
        }
        catch (DuplicateKeyException e) {
            // RequestParam 데이터와 동일한 아이디의 회원이 있을 경우
            return ApiResponse.fail(ErrorCode.BAD_REQUEST, "동일한 아이디의 회원이 이미 존재합니다.");
        } catch (Exception e) {
            return ApiResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * 로그인
     * @body loginMember 로그인 할 회원의 정보
     * @return ApiResponse
     */
    @PostMapping("/loginReq")
    public ApiResponse<Boolean> LoginReq(@RequestBody MemberFunctionRequest loginMember) {
        try {
            return ApiResponse.response(ResponseCode.OK, memberService.login(loginMember));
        }
        catch (NoSuchElementException e) {
            // RequestParam 데이터에 조회되지 않는 데이터 있는 경우. 이 경우에는 아이디 조회 안되는거라 이런 메시지..~
            return ApiResponse.fail(ErrorCode.BAD_REQUEST, "동일한 아이디의 회원이 이미 존재합니다.");
        } catch (Exception e) {
            return ApiResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * 회원탈퇴
     * @body withdrawalMember 회원탈퇴 할 회원의 정보
     * @return ApiResponse
     */
    @PostMapping("/withdrawalReq")
    public ApiResponse<Boolean> WithdrawalReq(@RequestBody MemberFunctionRequest withdrawalMember) {
        try {
            //true면 정상 삭제, false면 무언가에 의해 삭제 안됨
            return ApiResponse.response(ResponseCode.OK, memberService.withdrawal(withdrawalMember));
        }
        catch (NoSuchElementException e) {
            // RequestParam 데이터에 조회되지 않는 데이터 있는 경우
            return ApiResponse.fail(ErrorCode.NOT_FOUND, "조회되지 않는 데이터가 포함되어 있습니다.");
        } catch (AuthenticationException e) {
            return ApiResponse.fail(ErrorCode.BAD_REQUEST, "비밀번호가 일치하지 않습니다.");
        } catch (Exception e) {
            return ApiResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * 개인정보조회
     * @body inquiryMember 개인정보를 조회 할 회원의 정보
     * @return ApiResponse
     */
    @PostMapping("/memberInfoInquiryReq")
    public ApiResponse<MemberResponse> MemberInfoInquiryReq(@RequestBody MemberFunctionRequest inquiryMember) {
        try {
            MemberResponse inquiryResult = MemberResponse.of(memberService.memberInfoInquiry(inquiryMember));

            return ApiResponse.response(ResponseCode.OK, inquiryResult);
        }
        catch (NoSuchElementException e) {
            return ApiResponse.fail(ErrorCode.NOT_FOUND, "회원이 조회되지 않습니다.");
        } catch (AuthenticationException e) {
            return ApiResponse.fail(ErrorCode.BAD_REQUEST, "비밀번호가 일치하지 않습니다.");
        } catch (Exception e) {
            return ApiResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * 개인정보수정
     * @body newInfo 수정 할 회원의 새 정보
     * @return ApiResponse
     */
    @PostMapping("/memberInfoUpdateReq")
    public ApiResponse<MemberResponse> MemberInfoUpdateReq(@RequestBody MemberRequest newInfo) {
        try {
            MemberResponse updateResult = MemberResponse.of(memberService.memberInfoUpdate(newInfo));

            return ApiResponse.response(ResponseCode.OK, updateResult);
        }
        catch (NoSuchElementException e) {
            return ApiResponse.fail(ErrorCode.NOT_FOUND, "조회되지 않는 데이터가 포함되어있습니다.");
        } catch (Exception e) {
            return ApiResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
