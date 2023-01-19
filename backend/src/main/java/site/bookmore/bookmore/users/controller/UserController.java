package site.bookmore.bookmore.users.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
