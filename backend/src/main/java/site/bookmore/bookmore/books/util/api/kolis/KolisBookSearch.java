package site.bookmore.bookmore.books.util.api.kolis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;
import site.bookmore.bookmore.books.entity.Book;
import site.bookmore.bookmore.books.util.api.BookSearch;
import site.bookmore.bookmore.books.util.api.kolis.dto.Doc;
import site.bookmore.bookmore.books.util.api.kolis.dto.KolisSearchParams;
import site.bookmore.bookmore.books.util.api.kolis.dto.KolisSearchResponse;
import site.bookmore.bookmore.books.util.mapper.BookMapper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@Component
public class KolisBookSearch implements BookSearch<KolisSearchParams> {
    private static final String BASE_URL = "https://www.nl.go.kr";
    private static final String SEARCH_ENDPOINT = "/seoji/SearchApi.do";
    private final String token;

    private final WebClient webClient = WebClient.create(BASE_URL);

    public KolisBookSearch(@Value("${api.token.kolis}") String kolisToken) {
        this.token = kolisToken;
    }

    public Mono<Page<Book>> search(KolisSearchParams kolisSearchParams) {
        return webClient.get()
                .uri(uriBuilder -> buildUri(uriBuilder, kolisSearchParams))
                .retrieve()
                .bodyToMono(KolisSearchResponse.class)
                .map(kolisSearchResponse -> {
                    Pageable pageable = PageRequest.of(kolisSearchParams.getPage_no(), kolisSearchParams.getPage_size());
                    List<Book> books = kolisSearchResponse.getDocs().stream()
                            .map(BookMapper::of).collect(Collectors.toList());
                    return new PageImpl<>(books, pageable, kolisSearchResponse.getTotalCount());
                });
    }

    public Mono<Book> searchByISBN(String isbn) {
        StopWatch stopWatch = new StopWatch();
        KolisSearchParams kolisSearchParams = KolisSearchParams.builder()
                .isbn(isbn)
                .build();

        return webClient.get()
                .uri(uriBuilder -> buildUri(uriBuilder, kolisSearchParams))
                .header(AUTHORIZATION, token)
                .retrieve()
                .bodyToMono(KolisSearchResponse.class)
                .map(kolisSearchResponse -> {
                    List<Doc> docs = kolisSearchResponse.getDocs();
                    if (docs == null || docs.size() != 1) return new Book();
                    return BookMapper.of(docs.get(0));
                })
                .doOnSubscribe(subscription -> {
                    log.info("국립중앙도서관 도서 상세조회");
                    stopWatch.start();
                })
                .doOnSuccess((book) -> {
                    stopWatch.stop();
                    log.info("국립중앙도서관 도서 상세조회 완료 [{}ms]", stopWatch.getTotalTimeMillis());
                });
    }

    private URI buildUri(UriBuilder uriBuilder, KolisSearchParams kolisSearchParams) {
        uriBuilder.path(SEARCH_ENDPOINT);
        uriBuilder.queryParam("cert_key", token)
                .queryParam("result_style", "json");

        Class<?> clazz = kolisSearchParams.getClass();
        Method[] methods = clazz.getDeclaredMethods();

        try {
            for (Method method : methods) {
                String name = method.getName();
                if (!name.startsWith("get")) continue;
                String fieldName = name.substring(3).toLowerCase();
                Object value = method.invoke(kolisSearchParams);
                if (value != null) uriBuilder.queryParam(fieldName, value);
            }
        } catch (InvocationTargetException | IllegalAccessException e) {
            // InvocationTargetException : KolisSearchParams 필드가 null인경우 api 호출시 제외됨
        }
        return uriBuilder.build();
    }
}
