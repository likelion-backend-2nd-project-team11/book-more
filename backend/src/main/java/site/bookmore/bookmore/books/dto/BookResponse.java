package site.bookmore.bookmore.books.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bookmore.bookmore.books.entity.Book;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookResponse {
    private String isbn;
    private String title;
    private String image;

    public static BookResponse of(Book book) {
        return BookResponse.builder()
                .isbn(book.getId())
                .title(book.getTitle())
                .image(book.getImage())
                .build();
    }
}
