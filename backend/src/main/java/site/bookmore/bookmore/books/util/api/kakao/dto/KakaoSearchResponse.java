package site.bookmore.bookmore.books.util.api.kakao.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class KakaoSearchResponse {
    private Meta meta;
    private List<Document> documents;
}
