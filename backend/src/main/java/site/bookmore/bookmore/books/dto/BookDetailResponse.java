package site.bookmore.bookmore.books.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bookmore.bookmore.books.entity.Author;
import site.bookmore.bookmore.books.entity.Book;
import site.bookmore.bookmore.books.entity.Subject;
import site.bookmore.bookmore.books.entity.Translator;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookDetailResponse {
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

    public static BookDetailResponse of(Book book) {
        return BookDetailResponse.builder()
                .isbn(book.getId())
                .title(book.getTitle())
                .authors(book.getAuthors().stream().map(Author::getName).collect(Collectors.toList()))
                .translators(book.getTranslators().stream().map(Translator::getName).collect(Collectors.toList()))
                .subject(book.getSubject())
                .publisher(book.getPublisher())
                .pages(book.getPages())
                .image(book.getImage())
                .chapter(book.getChapter())
                .introduce(book.getIntroduce())
                .price(book.getPrice())
                .build();
    }
}
