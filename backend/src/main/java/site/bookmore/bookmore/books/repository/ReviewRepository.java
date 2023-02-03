package site.bookmore.bookmore.books.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.bookmore.bookmore.books.entity.Book;
import site.bookmore.bookmore.books.entity.Review;
import site.bookmore.bookmore.users.entity.User;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findByBookAndDeletedDatetimeIsNull(Pageable pageable, Book book);

    Page<Review> findByAuthorAndDeletedDatetimeIsNull(Pageable pageable, User user);

    Optional<Review> findByIdAndDeletedDatetimeIsNull(Long id);

    @Query(value = "SELECT sum (r.likesCount) FROM Review r WHERE r.author.id = :id ")
    Integer findSum(@Param("id") Long id);
}