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
import org.springframework.web.multipart.MultipartFile;
import site.bookmore.bookmore.common.dto.ResultResponse;
import site.bookmore.bookmore.common.support.annotation.Authorized;
import site.bookmore.bookmore.reviews.dto.ReviewPageResponse;
import site.bookmore.bookmore.reviews.service.ReviewService;
import site.bookmore.bookmore.users.dto.*;
import site.bookmore.bookmore.users.service.UserService;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@Api(tags = "1-회원")
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ReviewService reviewService;


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
    @ApiOperation(value = "특정 유저 회원 정보 수정")
    @PostMapping("/{id}")
    public ResultResponse<UserResponse> update(@RequestBody @Valid UserUpdateRequest userUpdateRequest, @PathVariable Long id, @ApiIgnore Authentication authentication) {
        String email = authentication.getName();
        return ResultResponse.success(userService.infoUpdate(email, id, userUpdateRequest));
    }

    @Authorized
    @ApiOperation(value = "특정 유저 회원 탈퇴")
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
    public ResultResponse<UserJoinResponse> verify(@ApiIgnore Authentication authentication) {
        String email = authentication.getName();
        return ResultResponse.success(userService.verify(email));
    }

    @Authorized
    @ApiOperation(value = "내 정보 수정")
    @PostMapping("/me")
    public ResultResponse<UserPersonalResponse> update(@Valid @RequestBody UserUpdateRequest userUpdateRequest, @ApiIgnore Authentication authentication) {
        String email = authentication.getName();
        return ResultResponse.success(userService.infoEdit(email, userUpdateRequest));
    }

    @Authorized
    @ApiOperation(value = "내 정보 조회")
    @GetMapping("/me")
    public ResultResponse<UserPersonalResponse> searchMyProfile(@ApiIgnore Authentication authentication) {
        String email = authentication.getName();
        UserPersonalResponse userPersonalResponse = userService.search(email);
        return ResultResponse.success(userPersonalResponse);
    }

    @Authorized
    @ApiOperation(value = "회원 탈퇴")
    @DeleteMapping("/me")
    public ResultResponse<UserResponse> deleteMe(@ApiIgnore Authentication authentication) {
        String email = authentication.getName();
        return ResultResponse.success(userService.delete(email));
    }

    @ApiOperation(value = "회원 상세 정보")
    @GetMapping("/{id}")
    public ResultResponse<UserDetailResponse> detail(@PathVariable Long id) {
        return ResultResponse.success(userService.detail(id));
    }

    @Authorized
    @ApiOperation(value = "회원 프로필 사진 변경")
    @PostMapping("/me/profile")
    public ResultResponse<UserProfileResponse> updateProfile(@RequestPart MultipartFile multipartFile, @ApiIgnore Authentication authentication) throws IOException {
        String email = authentication.getName();
        String userNickname = userService.updateProfile(multipartFile, email);
        return ResultResponse.success(new UserProfileResponse(userNickname, "프로필 사진 변경 완료"));
    }

    @Authorized
    @ApiOperation(value = "회원 프로필 기본 사진으로 변경")
    @DeleteMapping("/me/profile")
    public ResultResponse<UserProfileResponse> updateProfileDefault(@ApiIgnore Authentication authentication) {
        String email = authentication.getName();
        String userNickname = userService.updateProfileDefault(email);
        return ResultResponse.success(new UserProfileResponse(userNickname, "프로필 기본 사진으로 변경 완료"));
    }


    @ApiOperation(value = "회원 리뷰 조회")
    @GetMapping("/{id}/reviews")
    public ResultResponse<Page<ReviewPageResponse>> findReviewsByAuthor(@ApiIgnore @PageableDefault(size = 20, sort = "createdDatetime", direction = Sort.Direction.DESC) Pageable pageable, @PathVariable Long id) {
        return ResultResponse.success(reviewService.findByAuthor(id, pageable));
    }
}




