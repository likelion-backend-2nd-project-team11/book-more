package site.bookmore.bookmore.books.util.api.kakao.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class KakaoSearchResponse {
    private Meta meta;
    private List<Document> documents;
}
