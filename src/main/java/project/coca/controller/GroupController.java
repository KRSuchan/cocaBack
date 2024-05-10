package project.coca.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.oxm.ValidationFailureException;
import org.springframework.web.bind.annotation.*;
import project.coca.domain.group.CoGroup;
import project.coca.domain.personal.Member;
import project.coca.domain.tag.Tag;
import project.coca.dto.request.GroupRequest;
import project.coca.dto.request.GroupUpdateRequest;
import project.coca.dto.response.common.ApiResponse;
import project.coca.dto.response.common.error.ErrorCode;
import project.coca.dto.response.common.success.ResponseCode;
import project.coca.dto.response.group.GroupResponse;
import project.coca.service.GroupService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/group")
public class GroupController {
    private final GroupService groupService;

    /**
     * 07. 캘린더 목록 조회 (참가중인 그룹 목록 조회)
     */
    @GetMapping("/member/{memberId}")
    public ApiResponse<?> getMemberId(@PathVariable String memberId) {
        log.info("getMemberId {}", memberId);
        try {
            List<CoGroup> groups = groupService.findJoinedGroups(memberId);
            List<GroupResponse> data = groups
                    .stream()
                    .map(GroupResponse::of)
                    .collect(Collectors.toList());
            return ApiResponse.response(ResponseCode.OK, data);
        } catch (NoSuchElementException e) {
            return ApiResponse.fail(ErrorCode.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            return ApiResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * alpha 그룹에 참가하기
     */
    @PostMapping("/join/member/{memberId}/group/{groupId}")
    public ApiResponse<GroupResponse> joinGroup(@PathVariable String memberId,
                                                @PathVariable Long groupId) {
        log.info("joinGroup : member {}", memberId);
        log.info("joinGroup : group {}", groupId);
        try {
            groupService.joinGroup(memberId, groupId);
            return ApiResponse.success(ResponseCode.OK, "그룹 참가 완료");
        } catch (NoSuchElementException e) {
            return ApiResponse.fail(ErrorCode.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            return ApiResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * alpha 그룹 탈퇴하기
     */
    @PostMapping("/leave/member/{memberId}/group/{groupId}")
    public ApiResponse<GroupResponse> leaveGroup(@PathVariable String memberId,
                                                 @PathVariable Long groupId) {
        log.info("leaveGroup : member {}", memberId);
        log.info("leaveGroup : group {}", groupId);
        try {
            groupService.leaveGroup(memberId, groupId);
            return ApiResponse.success(ResponseCode.OK, "그룹 나가기 완료");
        } catch (NoSuchElementException e) {
            return ApiResponse.fail(ErrorCode.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            return ApiResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

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
     * 22-1. 그룹 검색 by groupName
     */
    @GetMapping("/find/include/groupName")
    public ApiResponse<List<GroupResponse>> findGroupsByGroupName(@RequestParam String groupName) {
        log.info("Search group by title: {}", groupName);
        List<CoGroup> groups = groupService.findGroupsByNameLike(groupName);
        List<GroupResponse> data = groups
                .stream()
                .map(GroupResponse::of)
                .collect(Collectors.toList());
        return ApiResponse.response(ResponseCode.OK, data);
    }

    /**
     * 22-2. 그룹 검색 by tag
     */
    @GetMapping("/find/tag")
    public ApiResponse<List<GroupResponse>> findGroupsByTag(@RequestParam Tag tag) {
        log.info("Search group by tag: {}", tag);
        List<CoGroup> groups = groupService.findGroupsByTag(tag);
        List<GroupResponse> data = groups
                .stream()
                .map(GroupResponse::of)
                .collect(Collectors.toList());
        return ApiResponse.response(ResponseCode.OK, data);
    }

    /**
     * 23. 그룹 상세 정보 조회
     */
    @GetMapping("/detail")
    public ApiResponse<GroupResponse> findGroupDetail(@RequestParam Long groupId) {
        log.info("find group detail for search by groupId: {}", groupId);
        try {
            CoGroup group = groupService.findGroupById(groupId);
            GroupResponse data = GroupResponse.of(group);
            return ApiResponse.response(ResponseCode.OK, data);
        } catch (NoSuchElementException e) {
            return ApiResponse.fail(ErrorCode.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            return ApiResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * 25. 그룹 수정
     */
    @PutMapping("/update")
    public ApiResponse<GroupResponse> updateGroup(@RequestBody GroupUpdateRequest request) {
        log.info("update group: {}", request);
        try {
            CoGroup updateInfo = new CoGroup();
            updateInfo.setGroupManager(request.getManagers());
            updateInfo.setName(request.getGroupName());
            updateInfo.setDescription(request.getDescription());
            groupService.updateGroup(request.getGroupId(), request.getAdminId(), updateInfo);
            return ApiResponse.success(ResponseCode.OK, "수정 완료");
        } catch (NoSuchElementException e) {
            return ApiResponse.fail(ErrorCode.NOT_FOUND, e.getMessage());
        } catch (ValidationFailureException e) {
            return ApiResponse.fail(ErrorCode.UNAUTHORIZED, e.getMessage());
        } catch (Exception e) {
            return ApiResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * 26. 그룹 삭제
     */
    @DeleteMapping("/delete")
    public ApiResponse<GroupResponse> deleteGroup(@RequestParam String adminId, @RequestParam Long groupId) {
        log.info("delete group admin: {}", adminId);
        log.info("delete group: {}", groupId);
        try {
            groupService.deleteGroup(adminId, groupId);
            return ApiResponse.success(ResponseCode.OK, "삭제 완료");
        } catch (NoSuchElementException e) {
            return ApiResponse.fail(ErrorCode.NOT_FOUND, e.getMessage());
        } catch (ValidationFailureException e) {
            return ApiResponse.fail(ErrorCode.UNAUTHORIZED, e.getMessage());
        } catch (Exception e) {
            return ApiResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
