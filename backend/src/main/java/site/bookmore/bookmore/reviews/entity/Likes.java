package site.bookmore.bookmore.reviews.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bookmore.bookmore.common.entity.BaseEntity;
import site.bookmore.bookmore.users.entity.User;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "likes")
@Getter
public class Likes extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean liked;

    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(name = "review_id", foreignKey = @ForeignKey(name = "fk_likes_review"))
    private Review review;

    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_likes_user"))
    private User user;

    public static Likes of(User user, Review review) {
        return Likes.builder()
                .user(user)
                .review(review)
                .build();
    }

    public boolean likes() {
        if (liked) {
            review.unlikes();
        } else review.likes();

        toggleLikes();
        return liked;
    }

    private void toggleLikes() {
        liked = !liked;
    }
}