package site.bookmore.bookmore.reviews.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.bookmore.bookmore.reviews.entity.Likes;
import site.bookmore.bookmore.reviews.entity.Review;
import site.bookmore.bookmore.users.entity.User;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    Optional<Likes> findByUserAndReview(User user, Review review);
}