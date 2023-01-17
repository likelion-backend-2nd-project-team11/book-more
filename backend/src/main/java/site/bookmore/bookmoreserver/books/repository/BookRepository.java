package site.bookmore.bookmoreserver.books.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.bookmore.bookmoreserver.books.entity.Book;

public interface BookRepository extends JpaRepository<Book, String> {
}
