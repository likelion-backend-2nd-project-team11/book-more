package site.bookmore.bookmore.books.util.api;

import org.springframework.data.domain.Page;
import reactor.core.publisher.Mono;
import site.bookmore.bookmore.books.entity.Book;

public interface BookSearch<T> {
    Mono<Page<Book>> search(T searchParams);

    Mono<Book> searchByISBN(String isbn);
}
