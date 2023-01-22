package site.bookmore.bookmore.books.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bookmore.bookmore.books.entity.Review;

import java.time.format.DateTimeFormatter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ReviewPageResponse {
    private Long id;
    private String author;
    private String body;
    private boolean spoiler;
    private int professionalism;
    private int fun;
    private int readability;
    private int collectible;
    private int difficulty;
    private String createdDatetime;

    public ReviewPageResponse(Review review) {
        this.id = review.getId();
        this.author = review.getAuthor().getNickname();
        this.body = review.getBody();
        this.spoiler = review.isSpoiler();
        this.professionalism = review.getChart().getProfessionalism();
        this.fun = review.getChart().getFun();
        this.readability = review.getChart().getReadability();
        this.collectible = review.getChart().getCollectible();
        this.difficulty = review.getChart().getDifficulty();
        this.createdDatetime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(review.getCreatedDatetime());
    }
}