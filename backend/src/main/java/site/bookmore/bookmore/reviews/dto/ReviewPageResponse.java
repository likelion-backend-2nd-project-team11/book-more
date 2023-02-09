package site.bookmore.bookmore.reviews.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bookmore.bookmore.reviews.entity.Review;

import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ReviewPageResponse {
    private Long id;
    private Long userId;
    private String nickname;
    private String isbn;
    private String title;
    private String body;
    @Builder.Default
    private Set<TagResponse> tags = new HashSet<>();
    private boolean spoiler;
    private int professionalism;
    private int fun;
    private int readability;
    private int collectible;
    private int difficulty;
    private int likesCount;
    private String createdDatetime;

    public static ReviewPageResponse of(Review review) {
        return ReviewPageResponse.builder()
                .id(review.getId())
                .userId(review.getAuthor().getId())
                .nickname(review.getAuthor().getNickname())
                .isbn(review.getBook().getId())
                .title(review.getBook().getTitle())
                .body(review.getBody())
                .tags(review.getTags().stream().map(TagResponse::of).collect(Collectors.toSet()))
                .spoiler(review.getSpoiler())
                .professionalism(review.getChart().getProfessionalism())
                .fun(review.getChart().getFun())
                .readability(review.getChart().getReadability())
                .collectible(review.getChart().getCollectible())
                .difficulty(review.getChart().getDifficulty())
                .likesCount(review.getLikesCount())
                .createdDatetime(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(review.getCreatedDatetime()))
                .build();
    }
}