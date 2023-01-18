package site.bookmore.bookmore.books.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.bookmore.bookmore.books.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}