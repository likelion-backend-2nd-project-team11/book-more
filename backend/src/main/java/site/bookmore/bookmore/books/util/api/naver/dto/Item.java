package site.bookmore.bookmore.books.util.api.naver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Item {
    private String title;
    private String link;
    private String image;
    private String author;
    private Integer discount;
    private String publisher;
    private String pubdate;
    private String isbn;
    private String description;
}
