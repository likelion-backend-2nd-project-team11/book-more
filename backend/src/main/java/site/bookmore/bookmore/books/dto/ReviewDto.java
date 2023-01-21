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
public class ReviewDto {
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

    public static ReviewDto of(Review review) {
        return ReviewDto.builder()
                .id(review.getId())
                .author(review.getAuthor().getNickname())
                .body(review.getBody())
                .spoiler(review.isSpoiler())
                .professionalism(review.getChart().getProfessionalism())
                .fun(review.getChart().getFun())
                .readability(review.getChart().getReadability())
                .collectible(review.getChart().getCollectible())
                .difficulty(review.getChart().getDifficulty())
                .createdDatetime(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(review.getCreatedDatetime()))
                .build();
    }
}