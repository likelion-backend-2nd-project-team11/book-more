package site.bookmore.bookmore.reviews.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bookmore.bookmore.books.entity.Book;
import site.bookmore.bookmore.common.entity.BaseEntity;
import site.bookmore.bookmore.users.entity.User;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Book book;

    private String body;
    private Boolean spoiler;

    @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "chart_id")
    private Chart chart;

    private int likesCount;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "review", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @Builder.Default
    private Set<ReviewTag> reviewTags = new HashSet<>();

    public Set<Tag> getTags() {
        return reviewTags.stream().map(ReviewTag::getTag).collect(Collectors.toSet());
    }

    public Set<String> extractTagsLabel() {
        return reviewTags.stream().map(reviewTag -> reviewTag.getTag().getLabel()).collect(Collectors.toSet());
    }

    // 도서 리뷰 수정
    public void update(Review review) {
        updateBody(review.getBody());
        updateSpoiler(review.getSpoiler());
        updateChart(review.getChart());
    }

    public void removeReviewTag(ReviewTag reviewTag) {
        this.reviewTags.remove(reviewTag);
    }

    private void updateBody(String body) {
        if (body != null) {
            this.body = body;
        }
    }

    private void updateSpoiler(Boolean spoiler) {
        if (spoiler != null) {
            this.spoiler = spoiler;
        }
    }

    private void updateChart(Chart chart) {
        if (chart != null) {
            this.chart.update(chart);
        }
    }

    public synchronized void likes() {
        likesCount++;
    }

    public synchronized void unlikes() {
        if (likesCount > 0) {
            likesCount--;
        }
    }
}