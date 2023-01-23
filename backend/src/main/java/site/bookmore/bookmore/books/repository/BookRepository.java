package site.bookmore.bookmore.books.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.bookmore.bookmore.books.entity.Book;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, String> {
    @Query("SELECT b FROM Book b JOIN FETCH b.authors JOIN FETCH  b.translators WHERE b.id=:isbn")
    Optional<Book> findById(@Param("isbn") String isbn);
}
