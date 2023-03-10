package site.bookmore.bookmore.alarms.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import site.bookmore.bookmore.alarms.dto.AlarmResponse;
import site.bookmore.bookmore.alarms.service.AlarmService;
import site.bookmore.bookmore.common.dto.ResultResponse;
import site.bookmore.bookmore.common.support.annotation.Authorized;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@Api(tags = "6-알림")
@RequestMapping("/api/v1/alarms")
@RequiredArgsConstructor
public class AlarmController {
    private final AlarmService alarmService;

    /**
     * 알림
     */
    @Authorized
    @ApiOperation(value = "나의 모든 알림 조회")
    @GetMapping("")
    public ResultResponse<Page<AlarmResponse>> getAlarm(@PageableDefault(size = 20, sort = "createdDatetime", direction = Sort.Direction.DESC) Pageable pageable, @ApiIgnore Authentication authentication) {
        Page<AlarmResponse> alarmResponses = alarmService.findByFollowingReview(pageable, authentication.getName());

        return ResultResponse.success(alarmResponses);
    }

    @Authorized
    @ApiOperation(value = "나의 새로운 알림 조회")
    @GetMapping("/new")
    public ResultResponse<Page<AlarmResponse>> getNewAlarms(@PageableDefault(size = 20, sort = "createdDatetime", direction = Sort.Direction.DESC) Pageable pageable, @ApiIgnore Authentication authentication) {
        Page<AlarmResponse> alarmResponses = alarmService.getNewAlarms(pageable, authentication.getName());

        return ResultResponse.success(alarmResponses);
    }

    @Authorized
    @ApiOperation(value = "알림 읽음 처리")
    @PostMapping("/{id}/confirm")
    public ResultResponse<String> doConfirm(@PathVariable Long id, @ApiIgnore Authentication authentication) {
        String email = authentication.getName();
        String result = alarmService.doConfirm(email, id);
        return ResultResponse.success(result);
    }
}