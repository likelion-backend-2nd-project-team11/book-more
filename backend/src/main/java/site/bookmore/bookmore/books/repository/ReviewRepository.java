package site.bookmore.bookmore.books.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import site.bookmore.bookmore.books.entity.Book;
import site.bookmore.bookmore.books.entity.Review;
import site.bookmore.bookmore.users.entity.User;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findByBook(Pageable pageable, Book book);

    Long countByAuthorAndDeletedDatetimeIsNull(User user);

    Page<Review> findByAuthorAndDeletedDatetimeIsNull(Pageable pageable, User user);
}