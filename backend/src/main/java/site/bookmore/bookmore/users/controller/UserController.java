package site.bookmore.bookmore.users.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import site.bookmore.bookmore.books.dto.ReviewPageResponse;
import site.bookmore.bookmore.common.dto.ResultResponse;
import site.bookmore.bookmore.common.support.annotation.Authorized;
import site.bookmore.bookmore.users.dto.*;
import site.bookmore.bookmore.users.service.UserService;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@RestController
@Api(tags = "1-회원")
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @ApiOperation(value = "회원가입")
    @PostMapping("/join")
    public ResultResponse<UserJoinResponse> join(@RequestBody @Valid UserJoinRequest userJoinRequest) {
        return ResultResponse.success(userService.join(userJoinRequest));
    }

    @ApiOperation(value = "로그인")
    @PostMapping("/login")
    public ResultResponse<UserLoginResponse> login(@RequestBody @Valid UserLoginRequest userLoginRequest) {
        return ResultResponse.success(userService.login(userLoginRequest));
    }

    @Authorized
    @ApiOperation(value = "회원 정보 수정")
    @PostMapping("/{id}")
    public ResultResponse<UserResponse> update(@RequestBody UserUpdateRequest userUpdateRequest, @PathVariable Long id, @ApiIgnore Authentication authentication) {
        String email = authentication.getName();
        return ResultResponse.success(userService.infoUpdate(email, id, userUpdateRequest));
    }

    @Authorized
    @ApiOperation(value = "회원 탈퇴")
    @DeleteMapping("/{id}")
    public ResultResponse<UserResponse> delete(@PathVariable Long id, @ApiIgnore Authentication authentication) {
        String email = authentication.getName();
        return ResultResponse.success(userService.delete(email, id));
    }

    /**
     * verify
     */
    @Authorized
    @ApiOperation(value = "회원 검증")
    @PostMapping("/verify")
    public ResultResponse<UserJoinResponse> verify(Authentication authentication) {
        String email = authentication.getName();
        return ResultResponse.success(userService.verify(email));
    }

    @Authorized
    @ApiOperation(value = "내 정보 수정")
    @PostMapping("/me")
    public ResultResponse<UserUpdateResponse> update(@RequestBody UserUpdateRequest userUpdateRequest, @ApiIgnore Authentication authentication) {
        String email = authentication.getName();
        return ResultResponse.success(userService.infoEdit(email, userUpdateRequest));
    }

    @Authorized
    @ApiOperation(value = "내 정보 조회")
    @GetMapping("/me")
    public ResultResponse<UserUpdateResponse> searchMyProfile(@ApiIgnore Authentication authentication) {
        String email = authentication.getName();
        UserUpdateResponse userUpdateResponse = userService.search(email);
        return ResultResponse.success(userUpdateResponse);

    @ApiOperation(value = "회원 상세 정보")
    @GetMapping("/{id}")
    public ResultResponse<UserDetailResponse> detail(@PathVariable Long id) {
        return ResultResponse.success(userService.detail(id));
    }
}




