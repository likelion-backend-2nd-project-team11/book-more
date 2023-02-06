package site.bookmore.bookmore.books.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.bookmore.bookmore.books.entity.Review;
import site.bookmore.bookmore.books.entity.ReviewTag;
import site.bookmore.bookmore.books.entity.Tag;

import java.util.Optional;

public interface ReviewTagRepository extends JpaRepository<ReviewTag, Long> {
    Optional<ReviewTag> findByReviewAndTag(Review review, Tag tag);
}