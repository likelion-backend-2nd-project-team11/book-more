package site.bookmore.bookmore.books.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bookmore.bookmore.books.entity.Book;
import site.bookmore.bookmore.books.entity.Review;
import site.bookmore.bookmore.users.entity.User;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ReviewRequest {
    private String body;
    private boolean spoiler;
    private int professionalism;
    private int fun;
    private int readability;
    private int collectible;
    private int difficulty;

    public Review toEntity(User user, Book book) {
        return Review.builder()
                .body(body)
                .spoiler(spoiler)
                .professionalism(professionalism)
                .fun(fun)
                .readability(readability)
                .collectible(collectible)
                .difficulty(difficulty)
                .build();
    }
}