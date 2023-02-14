package site.bookmore.bookmore.reviews.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bookmore.bookmore.books.entity.Book;
import site.bookmore.bookmore.common.support.annotation.ElementSize;
import site.bookmore.bookmore.reviews.entity.Review;
import site.bookmore.bookmore.users.entity.User;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ReviewRequest {
    @NotBlank(message = "본문은 반드시 작성되어야 하는 항목입니다.")
    @Size(max = 300, message = "본문은 300자 이하로 작성해 주세요.")
    private String body;
    private boolean spoiler;
    @Valid
    private ChartRequest chart;
    @ElementSize(max = 10, message = "태그는 10자 이하로 입력해주세요.")
    private Set<String> tags;

    // 도서 리뷰 등록
    public Review toEntity(User user, Book book) {
        return Review.builder()
                .author(user)
                .book(book)
                .body(body)
                .spoiler(spoiler)
                .chart(chart.toEntity())
                .likesCount(0)
                .build();
    }

    // 도서 리뷰 수정
    public Review toEntity() {
        return Review.builder()
                .body(body)
                .spoiler(spoiler)
                .chart(chart == null ? null : chart.toEntity())
                .build();
    }
}