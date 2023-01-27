package site.bookmore.bookmore.books.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bookmore.bookmore.books.entity.Book;
import site.bookmore.bookmore.books.entity.Chart;
import site.bookmore.bookmore.books.entity.Review;
import site.bookmore.bookmore.users.entity.User;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ReviewRequest {
    private String body;
    private boolean spoiler;
    private Chart chart;

    // 도서 리뷰 등록
    public Review toEntity(User user, Book book) {
        return Review.builder()
                .author(user)
                .book(book)
                .body(body)
                .spoiler(spoiler)
                .chart(chart)
                .likesCount(0)
                .build();
    }

    // 도서 리뷰 수정
    public Review toEntity() {
        return Review.builder()
                .body(body)
                .spoiler(spoiler)
                .chart(chart)
                .build();
    }
}