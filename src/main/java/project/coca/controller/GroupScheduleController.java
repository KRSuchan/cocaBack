package project.coca.controller;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.coca.domain.personal.PersonalSchedule;
import project.coca.dto.response.common.ApiResponse;
import project.coca.dto.response.common.error.ErrorCode;
import project.coca.dto.response.common.success.ResponseCode;
import project.coca.dto.response.group.GroupScheduleResponse;
import project.coca.dto.response.group.GroupScheduleSummaryResponse;
import project.coca.dto.response.personalSchedule.PersonalScheduleSummaryResponse;
import project.coca.service.GroupScheduleService;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/api/group-schedule")
public class GroupScheduleController {
    private final GroupScheduleService groupScheduleService;

    /**
     * 그룹 일정 목록 조회
     * @param groupId  조회 할 그룹 id
     * @param memberId  회원 개인 id
     * @param startDate 예시 : 2024-05-01
     * @param endDate   예시 : 2024-05-31
     * @return ApiResponse
     * NOT_FOUND: memberId로 회원이 조회되지 않는경우
     * CREATED: 그 외 정상, 해당 기간 존재하는 일정 반환
     */
    @GetMapping("/groupScheduleSummaryReq")
    public ApiResponse<List<GroupScheduleSummaryResponse>> groupScheduleSummaryReq(
            @RequestParam Long groupId, @RequestParam String memberId, @RequestParam LocalDate startDate, @RequestParam LocalDate endDate)
    {
        try {
            List<GroupScheduleSummaryResponse> groupScheduleList = groupScheduleService.groupScheduleInquiry(groupId, memberId, startDate, endDate)
                    .stream()
                    .map(GroupScheduleSummaryResponse::of)
                    .collect(Collectors.toList());

            return ApiResponse.response(ResponseCode.OK, groupScheduleList);
        } catch (NoSuchElementException e) {
            // RequestParam 데이터로 검색되지 않은 데이터가 존재할 경우
            return ApiResponse.fail(ErrorCode.NOT_FOUND, "조회되지 않는 데이터가 포함되어 있습니다.");
        } catch (Exception e) {
            return ApiResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * 그룹 일정 상세 정보 조회
     * @param groupId  조회 할 그룹 id
     * @param memberId  회원 개인 id
     * @param inquiryDate 예시 : 2024-05-01
     * @return ApiResponse
     * NOT_FOUND: memberId로 회원이 조회되지 않는경우
     * CREATED: 그 외 정상, 해당 기간 존재하는 일정 반환
     */
    @GetMapping("/groupScheduleSpecificReq")
    public ApiResponse<List<GroupScheduleResponse>> groupScheduleSpecificReq(
            @RequestParam Long groupId, @RequestParam String memberId, @RequestParam LocalDate inquiryDate)
    {
        try {
            List<GroupScheduleResponse> groupScheduleList = groupScheduleService.groupScheduleInquiry(groupId, memberId, inquiryDate, inquiryDate)
                    .stream()
                    .map(GroupScheduleResponse::of)
                    .collect(Collectors.toList());

            return ApiResponse.response(ResponseCode.OK, groupScheduleList);
        } catch (NoSuchElementException e) {
            // RequestParam 데이터로 검색되지 않은 데이터가 존재할 경우
            return ApiResponse.fail(ErrorCode.NOT_FOUND, "조회되지 않는 데이터가 포함되어 있습니다.");
        } catch (Exception e) {
            return ApiResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
