package site.bookmore.bookmore.books.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import site.bookmore.bookmore.books.dto.BookDetailResponse;
import site.bookmore.bookmore.books.dto.BookResponse;
import site.bookmore.bookmore.books.dto.BookSearchParams;
import site.bookmore.bookmore.books.entity.Book;
import site.bookmore.bookmore.books.repository.BookRepository;
import site.bookmore.bookmore.books.util.api.kakao.KakaoBookSearch;
import site.bookmore.bookmore.books.util.api.kolis.KolisBookSearch;
import site.bookmore.bookmore.books.util.api.naver.NaverBooksearch;
import site.bookmore.bookmore.books.util.api.naver.dto.NaverSearchParams;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final KakaoBookSearch kakaoBookSearch;
    private final KolisBookSearch kolisBookSearch;
    private final NaverBooksearch naverBooksearch;

    public Page<BookResponse> search(BookSearchParams bookSearchParams) {
        Page<Book> response = naverBooksearch.search(NaverSearchParams.from(bookSearchParams)).block();
        return response.map(BookResponse::of);
    }

    @Transactional
    public BookDetailResponse searchByISBN(String isbn) {
        StopWatch timer = new StopWatch();
        timer.start();

        Optional<Book> bookOptional = bookRepository.findById(isbn);
        if (bookOptional.isPresent()) {
            timer.stop();
            log.info("총 응답 시간 : {}ms", timer.getTotalTimeMillis());
            return BookDetailResponse.of(bookOptional.get());
        }

        log.info("DB 내 도서 정보 없음");
        log.info("API 호출");

        List<Mono<Book>> requests = new ArrayList<>();

        requests.add(naverBooksearch.searchByISBN(isbn));
        requests.add(kakaoBookSearch.searchByISBN(isbn));
        requests.add(kolisBookSearch.searchByISBN(isbn));

        Flux<Book> bookFlux = Flux.merge(requests);
        Book book = bookFlux.subscribeOn(Schedulers.parallel())
                .reduce(new Book(), Book::merge)
                .block();

        bookRepository.save(book);

        timer.stop();
        log.info("총 응답 시간 : {}ms", timer.getTotalTimeMillis());
        return BookDetailResponse.of(book);
    }
}
