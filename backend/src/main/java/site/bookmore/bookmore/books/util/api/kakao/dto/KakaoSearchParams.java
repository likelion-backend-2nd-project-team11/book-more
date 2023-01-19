package site.bookmore.bookmore.books.util.api.kakao.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class KakaoSearchParams {
    private final String query;
    private final String sort;
    private final Integer page;
    private final Integer size;
    private final String target;

    @Builder
    public KakaoSearchParams(String query, String sort, Integer page, Integer size, String target) {
        this.query = query;
        this.sort = sort;
        this.page = page;
        this.size = size;
        this.target = target;
    }
}
