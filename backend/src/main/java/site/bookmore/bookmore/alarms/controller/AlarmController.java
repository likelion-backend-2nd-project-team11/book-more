package site.bookmore.bookmore.alarms.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    @ApiOperation(value = "나의 알림 조회")
    @GetMapping("")
    public ResultResponse<Page<AlarmResponse>> getAlarm(@PageableDefault(size = 20, sort = "createdDatetime", direction = Sort.Direction.DESC) Pageable pageable, @ApiIgnore Authentication authentication) {
        Page<AlarmResponse> alarmResponses = alarmService.findByFollowingReview(pageable, authentication.getName());

        return ResultResponse.success(alarmResponses);
    }

}
