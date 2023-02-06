package site.bookmore.bookmore.reviews.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import site.bookmore.bookmore.reviews.dto.ReviewPageResponse;
import site.bookmore.bookmore.reviews.dto.ReviewRequest;
import site.bookmore.bookmore.reviews.dto.ReviewResponse;
import site.bookmore.bookmore.reviews.service.ReviewService;
import site.bookmore.bookmore.common.dto.ResultResponse;
import site.bookmore.bookmore.common.support.annotation.Authorized;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@RestController
@Api(tags = "4-리뷰")
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    // 도서 리뷰 등록
    @Authorized
    @ApiOperation(value = "작성")
    @PostMapping("/{isbn}/reviews")
    public ResultResponse<ReviewResponse> create(@RequestBody @Valid ReviewRequest reviewRequest, @PathVariable String isbn, Authentication authentication) {
        String email = authentication.getName();
        Long id = reviewService.create(reviewRequest, isbn, email);
        return ResultResponse.success(new ReviewResponse(id, "리뷰 등록 완료"));
    }

    // 도서 리뷰 조회
    @ApiOperation(value = "조회")
    @GetMapping("/{isbn}/reviews")
    public ResultResponse<Page<ReviewPageResponse>> read(@PageableDefault(size = 5, sort = "createdDatetime", direction = Sort.Direction.DESC) Pageable pageable, @PathVariable String isbn) {
        Page<ReviewPageResponse> reviewPage = reviewService.read(pageable, isbn);
        return ResultResponse.success(reviewPage);
    }

    // 도서 리뷰 수정
    @Authorized
    @ApiOperation(value = "수정")
    @PatchMapping("/reviews/{id}")
    public ResultResponse<ReviewResponse> update(@RequestBody ReviewRequest reviewRequest, @PathVariable Long id, @ApiIgnore Authentication authentication) {
        String email = authentication.getName();
        Long result = reviewService.update(reviewRequest, id, email);
        return ResultResponse.success(new ReviewResponse(result, "리뷰 수정 완료"));
    }

    // 도서 리뷰 삭제
    @Authorized
    @ApiOperation(value = "삭제")
    @DeleteMapping("/reviews/{id}")
    public ResultResponse<ReviewResponse> delete(@PathVariable Long id, @ApiIgnore Authentication authentication) {
        String email = authentication.getName();
        Long result = reviewService.delete(id, email);
        return ResultResponse.success(new ReviewResponse(result, "리뷰 삭제 완료"));
    }

    // 도서 리뷰에 좋아요 | 취소
    @Authorized
    @ApiOperation(value = "좋아요 | 좋아요 취소")
    @PostMapping("/reviews/{id}/likes")
    public ResultResponse<String> likes(@PathVariable Long id, @ApiIgnore Authentication authentication) {
        String email = authentication.getName();
        boolean result = reviewService.doLikes(email, id);
        return ResultResponse.success(result ? "좋아요를 눌렀습니다." : "좋아요가 취소되었습니다.");
    }
}