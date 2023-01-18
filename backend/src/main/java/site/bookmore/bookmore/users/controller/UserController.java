package site.bookmore.bookmore.users.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.bookmore.bookmore.users.dto.Response;
import site.bookmore.bookmore.users.dto.UserJoinRequest;
import site.bookmore.bookmore.users.dto.UserJoinResponse;
import site.bookmore.bookmore.users.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest userJoinRequest) {
        return Response.success(userService.join(userJoinRequest));
    }
}
