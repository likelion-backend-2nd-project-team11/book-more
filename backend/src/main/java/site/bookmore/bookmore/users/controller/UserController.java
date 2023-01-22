package site.bookmore.bookmore.users.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import site.bookmore.bookmore.common.dto.ResultResponse;
import site.bookmore.bookmore.users.dto.*;
import site.bookmore.bookmore.users.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public ResultResponse<UserJoinResponse> join(@RequestBody UserJoinRequest userJoinRequest) {
        return ResultResponse.success(userService.join(userJoinRequest));
    }

    @PostMapping("/login")
    public ResultResponse<UserLoginResponse> login(@RequestBody UserLoginRequest userLoginRequest) {
        return ResultResponse.success(userService.login(userLoginRequest));
    }

    @PatchMapping("/{id}")
    public ResultResponse<UserResponse> update(@RequestBody UserUpdateRequest userUpdateRequest, @PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        return ResultResponse.success(userService.infoUpdate(email, id, userUpdateRequest));
    }


    @DeleteMapping("/{id}")
    public ResultResponse<UserResponse> delete(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        return ResultResponse.success(userService.delete(email, id));
    }
}
