package site.bookmore.bookmore.books.entity;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "review_tag")
@NoArgsConstructor
public class ReviewTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Review review;
    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = {CascadeType.PERSIST})
    private Tag tag;

    @Builder
    public ReviewTag(Review review, Tag tag) {
        this.review = review;
        this.tag = tag;
    }

    public static Set<ReviewTag> from(Review review, Set<Tag> tagSet) {
        return tagSet.stream().map(tag -> ReviewTag.builder()
                .review(review)
                .tag(tag)
                .build()
        ).collect(Collectors.toSet());
    }
}
