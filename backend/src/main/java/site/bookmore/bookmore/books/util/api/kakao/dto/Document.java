package site.bookmore.bookmore.books.util.api.kakao.dto;

import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
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
}
