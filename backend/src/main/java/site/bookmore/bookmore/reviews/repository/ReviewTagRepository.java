package site.bookmore.bookmore.reviews.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.bookmore.bookmore.reviews.entity.Review;
import site.bookmore.bookmore.reviews.entity.ReviewTag;
import site.bookmore.bookmore.reviews.entity.Tag;

import java.util.Optional;

public interface ReviewTagRepository extends JpaRepository<ReviewTag, Long> {
    Optional<ReviewTag> findByReviewAndTag(Review review, Tag tag);
}