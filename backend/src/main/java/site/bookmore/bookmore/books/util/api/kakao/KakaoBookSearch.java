package site.bookmore.bookmore.books.util.api.kakao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;
import site.bookmore.bookmore.books.util.api.kakao.dto.KakaoSearchParams;
import site.bookmore.bookmore.books.util.api.kakao.dto.KakaoSearchResponse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
public class KakaoBookSearch {
    private static final String BASE_URL = "http://dapi.kakao.com";
    private static final String SEARCH_ENDPOINT = "/v3/search/book";
    private final String token;

    private final WebClient webClient = WebClient.create(BASE_URL);

    public KakaoBookSearch(@Value("${api.token.kakao}") String kakaoToken) {
        this.token = kakaoToken;
    }

    // Todo. 타임아웃 시간 설정
    public Mono<KakaoSearchResponse> search(KakaoSearchParams kakaoSearchParams) {
        return webClient.get()
                .uri(uriBuilder -> buildUri(uriBuilder, kakaoSearchParams))
                .header(AUTHORIZATION, token)
                .retrieve()
                .bodyToMono(KakaoSearchResponse.class);
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
