package site.bookmore.bookmore.books.util.api.naver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static site.bookmore.bookmore.books.entity.Book.INTRODUCE_LENGTH;

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

    public String getDescription() {
        if (description.length() > INTRODUCE_LENGTH) return splitDescription(description);
        return description;
    }

    private String splitDescription(String original) {
        String result = original.substring(0, INTRODUCE_LENGTH);
        int idx = result.lastIndexOf('.');
        if (idx < 0) return result;
        return result.substring(0, idx);
    }
}
