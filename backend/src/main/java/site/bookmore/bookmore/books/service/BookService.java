package site.bookmore.bookmore.books.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import site.bookmore.bookmore.books.dto.BookResponse;
import site.bookmore.bookmore.books.entity.Book;
import site.bookmore.bookmore.books.repository.BookRepository;
import site.bookmore.bookmore.books.util.api.kakao.KakaoBookSearch;
import site.bookmore.bookmore.books.util.api.kakao.dto.KakaoSearchParams;
import site.bookmore.bookmore.books.util.api.kakao.dto.KakaoSearchResponse;
import site.bookmore.bookmore.books.util.api.kolis.KolisBookSearch;
import site.bookmore.bookmore.books.util.mapper.BookResponseMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final KakaoBookSearch kakaoBookSearch;
    private final KolisBookSearch kolisBookSearch;

    public Page<BookResponse> searchAtKakao(KakaoSearchParams kakaoSearchParams) {
        // Todo. 카카오 api 의존도 해결
        KakaoSearchResponse response = kakaoBookSearch.search(kakaoSearchParams).block();
        List<BookResponse> bookResponses = response.getDocuments().stream()
//                .filter(document -> document.getAuthors().size() > 0) 필요한 컬럼이 없는 데이터를 제외
                .map(BookResponseMapper::of).collect(Collectors.toList());
        return new PageImpl<>(bookResponses, Pageable.unpaged(), response.getMeta().getPageable_count());
    }

    @Transactional
    public BookResponse searchByISBN(String isbn) {
        StopWatch timer = new StopWatch();
        timer.start();

        Optional<Book> bookOptional = bookRepository.findById(isbn);
        if (bookOptional.isPresent()) {
            timer.stop();
            log.info("총 응답 시간 : {}ms", timer.getTotalTimeMillis());
            return BookResponseMapper.of(bookOptional.get());
        };

        log.info("DB 내 도서 정보 없음");
        log.info("API 호출");

        // api 요청 리스트 초기화
        List<Mono<Book>> requests = new ArrayList<>();

        // api 요청 추가
        requests.add(kakaoBookSearch.searchByISBN(isbn));
        requests.add(kolisBookSearch.searchByISBN(isbn));

        // api 병렬 요청
        Flux<Book> bookFlux = Flux.merge(requests);
        Book book = bookFlux.subscribeOn(Schedulers.parallel())
                .reduce(new Book(), Book::merge)
                .block();

        bookRepository.save(book);

        timer.stop();
        log.info("총 응답 시간 : {}ms", timer.getTotalTimeMillis());
        return BookResponseMapper.of(book);
    }
}
