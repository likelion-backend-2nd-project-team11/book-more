package site.bookmore.bookmore.books.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import site.bookmore.bookmore.books.dto.ReviewRequest;
import site.bookmore.bookmore.books.dto.ReviewResponse;
import site.bookmore.bookmore.books.service.ReviewService;
import site.bookmore.bookmore.common.dto.ResultResponse;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    // 도서 리뷰 등록
    @PostMapping("/{isbn}/reviews")
    public ResultResponse<ReviewResponse> create(@RequestBody ReviewRequest reviewRequest, @PathVariable String isbn, Authentication authentication) {
        String email = authentication.getName();
        Long id = reviewService.create(reviewRequest, isbn, email);
        return ResultResponse.success(new ReviewResponse(id, "리뷰 등록 완료"));
    }

    // 도서 리뷰에 좋아요 | 취소
    @PostMapping("/reviews/{id}/likes")
    public ResultResponse<String> likes(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        boolean result = reviewService.doLikes(email, id);
        return ResultResponse.success(result ? "좋아요를 눌렀습니다." : "좋아요가 취소되었습니다.");
    }
}