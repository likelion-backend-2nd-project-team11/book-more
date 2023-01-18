package site.bookmore.bookmore.alarms.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.bookmore.bookmore.alarms.entity.dto.AlarmResponse;
import site.bookmore.bookmore.alarms.service.AlarmService;
import site.bookmore.bookmore.common.dto.ResultResponse;

@RestController
@RequestMapping("/api/v1/alarms")
@RequiredArgsConstructor
public class AlarmController {
    private final AlarmService alarmService;

    // 팔로잉의 리뷰 등록 알림 - GET /api/v1/alarms/reviews
    @GetMapping("/reviews")
    public ResultResponse<Page<AlarmResponse>> getFollowingReview(@PageableDefault(size = 20, sort = "createdDateTime", direction = Sort.Direction.DESC) Pageable pageable, Authentication authentication) {
        Page<AlarmResponse> alarmResponses = alarmService.findByFollowingReview(pageable, authentication.getName());

        return ResultResponse.success(alarmResponses);
    }

    // 나를 팔로잉 하면 알림 - GET /api/v1/alarms/follow

    // 내 리뷰에 좋아요 알림 - GET /api/v1/alarms/likes
}
