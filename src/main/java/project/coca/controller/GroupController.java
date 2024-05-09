package project.coca.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.coca.domain.group.CoGroup;
import project.coca.domain.personal.Member;
import project.coca.dto.request.GroupRequest;
import project.coca.dto.response.common.ApiResponse;
import project.coca.dto.response.common.error.ErrorCode;
import project.coca.dto.response.common.success.ResponseCode;
import project.coca.dto.response.group.GroupResponse;
import project.coca.service.GroupService;

import java.util.NoSuchElementException;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/group")
public class GroupController {
    private final GroupService groupService;

    /**
     * 21. 그룹 등록
     */
    @PostMapping("/add")
    public ApiResponse<GroupResponse> addGroup(@RequestBody GroupRequest request) {
        Member creator = request.getMember();
        CoGroup group = request.getGroup();
        log.info("Add group creator: {}", creator);
        log.info("Add group group: {}", group);
        try {
            CoGroup savedGroup = groupService.saveGroup(creator, group);
            GroupResponse data = GroupResponse.of(savedGroup);
            return ApiResponse.success(ResponseCode.CREATED, "그룹 정상 등록", data);
        } catch (NoSuchElementException e) {
            return ApiResponse.fail(ErrorCode.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            return ApiResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * 22-1. 그룹 검색 by title
     */
//    @GetMapping("/find/title")
//    public ApiResponse<List<GroupResponse>> findGroupsByTitle(@RequestParam String title) {
//        log.info("Search group by title: {}", title);
//
//    }

    /**
     * 23. 그룹 상세 정보 조회
     */

    /**
     * 24. 그룹 초대
     */

    /**
     * 25. 그룹 수정
     */

    /**
     * 26. 그룹 삭제
     */

}
