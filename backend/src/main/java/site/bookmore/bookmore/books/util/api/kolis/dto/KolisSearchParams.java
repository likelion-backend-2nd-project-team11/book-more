package site.bookmore.bookmore.books.util.api.kolis.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KolisSearchParams {
    @Builder.Default
    private int page_no = 1;
    @Builder.Default
    private int page_size = 20;
    private String isbn;
    private String title;
    private String author;
}
