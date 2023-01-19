package site.bookmore.bookmore.users.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import site.bookmore.bookmore.common.dto.ResultResponse;
import site.bookmore.bookmore.users.service.FollowService;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class FollowController {

    private final FollowService followService;

    @PostMapping("/{id}/follow")
    public ResultResponse<String> following(@PathVariable Long id, Authentication authentication, Principal principal) {
        String email = authentication.getName();
        return ResultResponse.success(followService.following(id, email));
    }

    @DeleteMapping("/{id}/follow")
    public ResultResponse<String> unfollowing(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        return ResultResponse.success(followService.unfollowing(id, email));
    }
}
