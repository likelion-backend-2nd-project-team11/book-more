package site.bookmore.bookmore.books.util.api.naver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import site.bookmore.bookmore.books.dto.BookSearchParams;

@Builder
@AllArgsConstructor
@Getter
public class NaverSearchParams {
    private String d_titl;
    private String d_isbn;
    private Integer display;
    private Integer start;
    private String sort;

    public static NaverSearchParams of(String isbn) {
        return NaverSearchParams.builder()
                .d_isbn(isbn)
                .build();
    }

    public static NaverSearchParams from(BookSearchParams bookSearchParams) {
        return NaverSearchParams.builder()
                .d_titl(bookSearchParams.getQuery())
                .display(bookSearchParams.getSize())
                .start(bookSearchParams.getPage())
                .build();
    }
}
