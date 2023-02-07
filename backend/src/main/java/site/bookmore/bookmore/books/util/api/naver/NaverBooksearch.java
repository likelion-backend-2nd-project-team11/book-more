package site.bookmore.bookmore.books.util.api.naver;

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
import site.bookmore.bookmore.books.util.api.naver.dto.Item;
import site.bookmore.bookmore.books.util.api.naver.dto.NaverSearchParams;
import site.bookmore.bookmore.books.util.api.naver.dto.NaverSearchResponse;
import site.bookmore.bookmore.books.util.mapper.BookMapper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class NaverBooksearch implements BookSearch<NaverSearchParams> {
    private static final String BASE_URL = "https://openapi.naver.com";
    private static final String SEARCH_ENDPOINT = "/v1/search/book_adv.json";
    private final String HEADER_CLIENT_ID = "X-Naver-Client-Id";
    private final String HEADER_CLIENT_SECRET = "X-Naver-Client-Secret";
    private final String clientId;
    private final String clientSecret;

    public NaverBooksearch(@Value("${api.token.naver.client.id}") String clientId, @Value("${api.token.naver.client.secret}") String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    private final WebClient webClient = WebClient.create(BASE_URL);

    @Override
    public Mono<Page<Book>> search(NaverSearchParams searchParams) {
        return webClient.get()
                .uri(uriBuilder -> buildUri(uriBuilder, searchParams))
                .header(HEADER_CLIENT_ID, clientId)
                .header(HEADER_CLIENT_SECRET, clientSecret)
                .retrieve()
                .bodyToMono(NaverSearchResponse.class)
                .map(naverSearchResponse -> {
                    Pageable pageable = PageRequest.of(searchParams.getStart(), searchParams.getDisplay());
                    List<Book> books = naverSearchResponse.getItems().stream()
                            .map(BookMapper::of).collect(Collectors.toList());
                    return new PageImpl<>(books, pageable, naverSearchResponse.getTotal());
                });
    }

    @Override
    public Mono<Book> searchByISBN(String isbn) {
        StopWatch stopWatch = new StopWatch();
        NaverSearchParams naverSearchParams = NaverSearchParams.of(isbn);

        return webClient.get()
                .uri(uriBuilder -> buildUri(uriBuilder, naverSearchParams))
                .header(HEADER_CLIENT_ID, clientId)
                .header(HEADER_CLIENT_SECRET, clientSecret)
                .retrieve()
                .bodyToMono(NaverSearchResponse.class)
                .map(naverSearchResponse -> {
                    List<Item> items = naverSearchResponse.getItems();
                    if (items == null || items.size() != 1) return new Book();
                    Item item = items.get(0);
                    Book book = BookMapper.of(item);
                    Book crawl = NaverBookCrawler.execute(item.getLink());
                    return book.merge(crawl);
                })
                .doOnSubscribe(subscription -> {
                    log.info("네이버 도서 상세조회");
                    stopWatch.start();
                })
                .doOnSuccess((book) -> {
                    stopWatch.stop();
                    log.info("네이버 도서 상세조회 완료 [{}ms]", stopWatch.getTotalTimeMillis());
                });
    }

    private URI buildUri(UriBuilder uriBuilder, NaverSearchParams naverSearchParams) {
        uriBuilder.path(SEARCH_ENDPOINT);
        Class<?> clazz = naverSearchParams.getClass();
        Method[] methods = clazz.getDeclaredMethods();

        try {
            for (Method method : methods) {
                String name = method.getName();
                if (!name.startsWith("get")) continue;
                String fieldName = name.substring(3).toLowerCase();
                Object value = method.invoke(naverSearchParams);
                if (value != null) uriBuilder.queryParam(fieldName, value);
            }
        } catch (InvocationTargetException | IllegalAccessException e) {
            // InvocationTargetException : 필드가 null인경우 api 호출시 제외됨
        }
        return uriBuilder.build();
    }
}
