package site.bookmore.bookmore.reviews.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "review_tag")
@NoArgsConstructor
@Getter
public class ReviewTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Review review;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Tag tag;

    @Builder
    public ReviewTag(Review review, Tag tag) {
        this.review = review;
        this.tag = tag;
    }

    public static ReviewTag of(Review review, Tag tag) {
        return ReviewTag.builder()
                .review(review)
                .tag(tag)
                .build();
    }

    public static Set<ReviewTag> of(Review review, Set<Tag> tagSet) {
        return tagSet.stream().map(tag -> ReviewTag.of(review, tag)).collect(Collectors.toSet());
    }
}
