package site.bookmore.bookmore.books.util.api.kolis.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import site.bookmore.bookmore.books.entity.Subject;

@Getter
public class Doc {
    @JsonProperty(value = "TITLE")
    private String title;
    @JsonProperty(value = "AUTHOR")
    private String author;
    @JsonProperty(value = "EA_ISBN")
    private String isbn;
    @JsonProperty(value = "EA_ADD_CODE")
    private String addCode;
    @JsonProperty(value = "PUBLISHER")
    private String publisher;
    @JsonProperty(value = "PRE_PRICE")
    private String price;
    @JsonProperty(value = "PAGE")
    private String page;
    @JsonProperty(value = "SUBJECT")
    private String subject;
    @JsonProperty(value = "EBOOK_YN")
    private String ebook;
    @JsonProperty(value = "TITLE_URL")
    private String image_url;
    @JsonProperty(value = "BOOK_TB_CNT_URL")
    private String chapter;
    @JsonProperty(value = "BOOK_INTRODUCTION_URL")
    private String introduction;
    @JsonProperty(value = "BOOK_SUMMARY_URL")
    private String summary;

    public Subject getSubject() {
        if (subject != null && !"".equals(subject)) return Subject.of(subject);
        if (addCode.length() == 5) return Subject.of(addCode.substring(2, 3));
        return null;
    }

    public Integer getPage() {
        page = page.replaceAll("[\\sa-zA-Z.]", "");
        return "".equals(page) ? null : Integer.parseInt(page);
    }
}
