package site.bookmore.bookmore.users.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import site.bookmore.bookmore.common.dto.ResultResponse;
import site.bookmore.bookmore.common.support.annotation.Authorized;
import site.bookmore.bookmore.users.dto.FollowerResponse;
import site.bookmore.bookmore.users.dto.FollowingResponse;
import site.bookmore.bookmore.users.service.FollowService;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
@Api(tags = "2-팔로우")
@RequestMapping("/api/v1/users")
public class FollowController {

    private final FollowService followService;

    @Authorized
    @ApiOperation(value = "팔로우")
    @PostMapping("/{id}/follow")
    public ResultResponse<String> following(@PathVariable Long id, @ApiIgnore Authentication authentication) {
        String email = authentication.getName();
        return ResultResponse.success(followService.following(id, email));
    }

    @Authorized
    @ApiOperation(value = "언팔로우")
    @DeleteMapping("/{id}/follow")
    public ResultResponse<String> unfollowing(@PathVariable Long id, @ApiIgnore Authentication authentication) {
        String email = authentication.getName();
        return ResultResponse.success(followService.unfollowing(id, email));
    }

    @ApiOperation(value = "팔로잉 조회")
    @GetMapping("/{id}/following")
    public ResultResponse<Page<FollowingResponse>> findAllFollowing(@PathVariable Long id, Pageable pageable) {
        return ResultResponse.success(followService.findAllFollowing(id, pageable));
    }

    @ApiOperation(value = "팔로잉 조회")
    @GetMapping("/{id}/follower")
    public ResultResponse<Page<FollowerResponse>> findAllFollower(@PathVariable Long id, Pageable pageable) {
        return ResultResponse.success(followService.findAllFollower(id, pageable));
    }
}
