package site.bookmore.bookmore.books.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import site.bookmore.bookmore.books.entity.Book;
import site.bookmore.bookmore.books.entity.Review;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findByBookAndDeletedDatetimeIsNull(Pageable pageable, Book book);

    Optional<Review> findByIdAndDeletedDatetimeIsNull(Long id);
}