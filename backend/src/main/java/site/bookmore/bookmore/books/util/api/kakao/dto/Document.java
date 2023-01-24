package site.bookmore.bookmore.books.util.api.kakao.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Document {
    private String title;
    private String contents;
    private String url;
    private String isbn;
    private OffsetDateTime datetime;
    private List<String> authors;
    private String publisher;
    private List<String> translators;
    private int price;
    private String thumbnail;
    private String status;

    public String getISBN() {
        String[] isbnArray = isbn.trim().split(" ");
        if (isbnArray.length == 0) return "";
        if (isbnArray.length < 2) return isbnArray[0];
        for (String s : isbnArray) {
            if (s.length() == 13) return s;
        }
        throw new IllegalArgumentException("잘못된 isbn입니다.");
    }
}
