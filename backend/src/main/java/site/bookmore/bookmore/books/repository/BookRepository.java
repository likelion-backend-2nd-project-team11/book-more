package site.bookmore.bookmore.books.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.bookmore.bookmore.books.entity.Book;

public interface BookRepository extends JpaRepository<Book, String> {
}
