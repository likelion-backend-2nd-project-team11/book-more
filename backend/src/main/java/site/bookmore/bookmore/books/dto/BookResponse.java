package site.bookmore.bookmore.books.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bookmore.bookmore.books.util.api.kakao.dto.Document;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookResponse {
    private String isbn;
    private String title;
    private List<String> authors;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> translators;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String kdc;
    private String publisher;
    private String image;
    private int price;

    public static BookResponse of(Document kakaoDocument){
        return BookResponse.builder()
                .isbn(kakaoDocument.getIsbn())
                .title(kakaoDocument.getTitle())
                .authors(kakaoDocument.getAuthors())
                .translators(kakaoDocument.getTranslators())
                .publisher(kakaoDocument.getPublisher())
                .image(kakaoDocument.getThumbnail())
                .price(kakaoDocument.getPrice())
                .build();
    }
}
