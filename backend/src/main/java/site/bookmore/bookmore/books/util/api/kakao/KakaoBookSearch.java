package site.bookmore.bookmore.books.util.api.kakao;

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
import site.bookmore.bookmore.books.util.api.kakao.dto.Document;
import site.bookmore.bookmore.books.util.api.kakao.dto.KakaoSearchParams;
import site.bookmore.bookmore.books.util.api.kakao.dto.KakaoSearchResponse;
import site.bookmore.bookmore.books.util.api.kakao.dto.Meta;
import site.bookmore.bookmore.books.util.mapper.BookMapper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@Component
public class KakaoBookSearch implements BookSearch<KakaoSearchParams> {
    private static final String BASE_URL = "http://dapi.kakao.com";
    private static final String SEARCH_ENDPOINT = "/v3/search/book";
    private final String token;

    private final WebClient webClient = WebClient.create(BASE_URL);

    public KakaoBookSearch(@Value("${api.token.kakao}") String kakaoToken) {
        this.token = kakaoToken;
    }

    // Todo. 타임아웃 시간 설정
    public Mono<Page<Book>> search(KakaoSearchParams kakaoSearchParams) {
        return webClient.get()
                .uri(uriBuilder -> buildUri(uriBuilder, kakaoSearchParams))
                .header(AUTHORIZATION, token)
                .retrieve()
                .bodyToMono(KakaoSearchResponse.class)
                .map(kakaoSearchResponse -> {
                    Meta meta = kakaoSearchResponse.getMeta();
                    Pageable pageable = PageRequest.of(kakaoSearchParams.getPage(), kakaoSearchParams.getSize());
                    List<Book> books = kakaoSearchResponse.getDocuments().stream()
                            .map(BookMapper::of).collect(Collectors.toList());
                    return new PageImpl<>(books, pageable, meta.getPageable_count());
                });
    }

    public Mono<Book> searchByISBN(String isbn) {
        StopWatch stopWatch = new StopWatch();
        KakaoSearchParams kakaoSearchParams = KakaoSearchParams.builder()
                .query(isbn)
                .target("isbn")
                .build();

        return webClient.get()
                .uri(uriBuilder -> buildUri(uriBuilder, kakaoSearchParams))
                .header(AUTHORIZATION, token)
                .retrieve()
                .bodyToMono(KakaoSearchResponse.class)
                .map(kakaoSearchResponse -> {
                    List<Document> documents = kakaoSearchResponse.getDocuments();
                    if (documents == null || documents.size() != 1) return new Book();
                    return BookMapper.of(documents.get(0));
                })
                .doOnSubscribe(subscription -> {
                    log.info("카카오 도서 상세조회");
                    stopWatch.start();
                })
                .doOnSuccess((book) -> {
                    stopWatch.stop();
                    log.info("카카오 도서 상세조회 완료 [{}ms]", stopWatch.getTotalTimeMillis());
                });
    }

    private URI buildUri(UriBuilder uriBuilder, KakaoSearchParams kakaoSearchParams) {
        uriBuilder.path(SEARCH_ENDPOINT);
        Class<?> clazz = kakaoSearchParams.getClass();
        Method[] methods = clazz.getDeclaredMethods();

        try {
            for (Method method : methods) {
                String name = method.getName();
                if (!name.startsWith("get")) continue;
                String fieldName = name.substring(3).toLowerCase();
                Object value = method.invoke(kakaoSearchParams);
                if (value != null) uriBuilder.queryParam(fieldName, value);
            }
        } catch (InvocationTargetException | IllegalAccessException e) {
            // InvocationTargetException : KakaoSearchParams 필드가 null인경우 api 호출시 제외됨
        }
        return uriBuilder.build();
    }
}
