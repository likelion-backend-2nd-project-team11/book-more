package site.bookmore.bookmore.books.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bookmore.bookmore.books.entity.Subject;

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
    private Subject subject;
    private String publisher;
    private Integer pages;
    private String image;
    private String chapter;
    private String introduce;
    private int price;
}
