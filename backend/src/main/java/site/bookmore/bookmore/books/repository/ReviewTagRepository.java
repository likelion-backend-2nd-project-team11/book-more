package site.bookmore.bookmore.books.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.bookmore.bookmore.books.entity.ReviewTag;

public interface ReviewTagRepository extends JpaRepository<ReviewTag, Long> {
}