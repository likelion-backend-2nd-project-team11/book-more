package site.bookmore.bookmore.books.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bookmore.bookmore.common.entity.BaseEntity;
import site.bookmore.bookmore.users.entity.User;

import javax.persistence.*;

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
    private boolean spoiler;

    @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "chart_id")
    private Chart chart;

    private int likesCount;

    // 도서 리뷰 수정
    public void update(Review review) {
        updateBody(review.getBody());
        updateSpoiler(review.isSpoiler());
        chart.update(review.getChart());
    }

    private void updateBody(String body) {
        if (body != null) {
            this.body = body;
        }
    }

    private void updateSpoiler(boolean spoiler) {
        this.spoiler = spoiler;
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