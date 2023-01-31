package site.bookmore.bookmore.books.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import reactor.core.publisher.Mono;
import site.bookmore.bookmore.books.dto.BookDetailResponse;
import site.bookmore.bookmore.books.dto.BookResponse;
import site.bookmore.bookmore.books.dto.BookSearchParams;
import site.bookmore.bookmore.books.entity.Book;
import site.bookmore.bookmore.books.entity.Subject;
import site.bookmore.bookmore.books.repository.BookRepository;
import site.bookmore.bookmore.books.util.api.kakao.KakaoBookSearch;
import site.bookmore.bookmore.books.util.api.kolis.KolisBookSearch;
import site.bookmore.bookmore.books.util.api.naver.NaverBooksearch;
import site.bookmore.bookmore.books.util.api.naver.dto.NaverSearchParams;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class BookServiceTest {
    private final BookRepository bookRepository = Mockito.mock(BookRepository.class);
    private final KakaoBookSearch kakaoBookSearch = Mockito.mock(KakaoBookSearch.class);
    private final KolisBookSearch kolisBookSearch = Mockito.mock(KolisBookSearch.class);
    private final NaverBooksearch naverBooksearch = Mockito.mock(NaverBooksearch.class);
    private final BookService bookService = new BookService(bookRepository, kakaoBookSearch, kolisBookSearch, naverBooksearch);

    @Test
    void search() {
        List<Book> books = Stream.of(1, 2, 3, 4, 5)
                .map(i ->
                        Book.builder()
                                .id("1000" + i)
                                .publisher("publisher" + i)
                                .title("title" + i)
                                .price(10000)
                                .build()
                ).collect(Collectors.toList());

        Page<Book> bookPage = new PageImpl<>(books);
        given(naverBooksearch.search(any(NaverSearchParams.class))).willReturn(Mono.just(bookPage));

        BookSearchParams bookSearchParams = BookSearchParams.of("title");

        Page<BookResponse> result = bookService.search(bookSearchParams);

        assertEquals(books.size(), result.getContent().size());
        verify(naverBooksearch).search(any(NaverSearchParams.class));
    }

    @Test
    void searchByISBN_from_db() {
        Book book = Book.builder()
                .id("10001")
                .title("title1")
                .publisher("publisher1")
                .price(10000)
                .build();
        given(bookRepository.findById("10001")).willReturn(Optional.of(book));

        BookDetailResponse result = bookService.searchByISBN("10001");

        assertEquals(book.getId(), result.getIsbn());
        assertEquals(book.getTitle(), result.getTitle());
        assertEquals(book.getPublisher(), result.getPublisher());
        assertEquals(book.getPrice(), result.getPrice());

        verify(bookRepository).findById("10001");
        verify(kakaoBookSearch, never()).searchByISBN(anyString());
        verify(kolisBookSearch, never()).searchByISBN(anyString());
    }

    @Test
    void searchByISBN_from_api() {
        Book book1 = Book.builder()
                .id("10001")
                .title("title1")
                .publisher("publisher1")
                .introduce("introduce")
                .build();

        Book book2 = Book.builder()
                .id("10001")
                .price(10000)
                .pages(100)
                .build();

        Book book3 = Book.builder()
                .id("10001")
                .price(10000)
                .pages(100)
                .subject(Subject.예술)
                .image("http://test.image.com/img.jpg")
                .build();

        given(bookRepository.findById("10001")).willReturn(Optional.empty());
        given(naverBooksearch.searchByISBN("10001")).willReturn(Mono.just(book1));
        given(kakaoBookSearch.searchByISBN("10001")).willReturn(Mono.just(book2));
        given(kolisBookSearch.searchByISBN("10001")).willReturn(Mono.just(book3));

        BookDetailResponse result = bookService.searchByISBN("10001");

        assertEquals(book1.getId(), result.getIsbn());
        assertEquals(book1.getTitle(), result.getTitle());
        assertEquals(book1.getPublisher(), result.getPublisher());
        assertEquals(book2.getPrice(), result.getPrice());
        assertEquals(book3.getPages(), result.getPages());
        assertEquals(book3.getImage(), result.getImage());

        verify(bookRepository).findById("10001");
        verify(naverBooksearch).searchByISBN(anyString());
        verify(kakaoBookSearch).searchByISBN(anyString());
        verify(kolisBookSearch).searchByISBN(anyString());
    }
}