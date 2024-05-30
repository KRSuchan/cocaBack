package project.coca.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.coca.dto.request.FindEmptyScheduleRequest;
import project.coca.dto.response.EmptySchedule;
import project.coca.dto.response.common.ApiResponse;
import project.coca.dto.response.common.error.ErrorCode;
import project.coca.dto.response.common.success.ResponseCode;
import project.coca.service.FindingAlgorithm.CommonScheduleService;

import java.util.List;
import java.util.NoSuchElementException;

@AllArgsConstructor
@RestController
@RequestMapping("/api/commonscheduleController")
public class CommonScheduleController {

    private final CommonScheduleService commonScheduleService;

    @GetMapping("/findEmptyScheduleReq")
    public ApiResponse<List<EmptySchedule>> findEmptyScheduleReq(@RequestBody FindEmptyScheduleRequest request) {
        try {
            List<EmptySchedule> result = commonScheduleService.findEmptySchedule(request);

            return ApiResponse.response(ResponseCode.OK, result);
        } catch(NoSuchElementException e) {
            return ApiResponse.fail(ErrorCode.NOT_FOUND, "찾을 일정의 시간/날을 입력하지 않았습니다.");
        } catch (Exception e) {
            return ApiResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
