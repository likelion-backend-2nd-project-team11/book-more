package site.bookmore.bookmore.users.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import site.bookmore.bookmore.common.dto.ResultResponse;
import site.bookmore.bookmore.users.dto.FollowerResponse;
import site.bookmore.bookmore.users.dto.FollowingResponse;
import site.bookmore.bookmore.users.dto.UserDetailResponse;
import site.bookmore.bookmore.users.service.FollowService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class FollowController {

    private final FollowService followService;

    @PostMapping("/{id}/follow")
    public ResultResponse<String> following(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        return ResultResponse.success(followService.following(id, email));
    }

    @DeleteMapping("/{id}/follow")
    public ResultResponse<String> unfollowing(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        return ResultResponse.success(followService.unfollowing(id, email));
    }

    @GetMapping("/{id}/following")
    public ResultResponse<Page<FollowingResponse>> findAllFollowing(@PathVariable Long id, Pageable pageable) {
        return ResultResponse.success(followService.findAllFollowing(id, pageable));
    }

    @GetMapping("/{id}/follower")
    public ResultResponse<Page<FollowerResponse>> findAllFollower(@PathVariable Long id, Pageable pageable) {
        return ResultResponse.success(followService.findAllFollower(id, pageable));
    }

    // 유저 아이디로 팔로워 수, 팔로잉 수, 리뷰 수 조회
    @GetMapping("/{id}")
    public ResultResponse<UserDetailResponse> getDetail(@PathVariable Long id) {
        UserDetailResponse response = followService.getDetail(id);
        return ResultResponse.success(response);
    }
}
