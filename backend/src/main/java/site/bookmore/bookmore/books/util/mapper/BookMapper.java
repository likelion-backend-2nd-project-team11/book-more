package site.bookmore.bookmore.books.util.mapper;

import site.bookmore.bookmore.books.entity.Author;
import site.bookmore.bookmore.books.entity.Book;
import site.bookmore.bookmore.books.entity.Translator;
import site.bookmore.bookmore.books.util.api.kakao.dto.Document;
import site.bookmore.bookmore.books.util.api.kolis.dto.Doc;
import site.bookmore.bookmore.books.util.api.naver.dto.Item;

import java.util.Set;
import java.util.stream.Collectors;

public class BookMapper {
    // 카카오
    public static Book of(Document document) {
        Set<Author> authors = document.getAuthors().stream()
                .map(Author::of)
                .collect(Collectors.toSet());

        Set<Translator> translators = document.getTranslators().stream()
                .map(Translator::of)
                .collect(Collectors.toSet());

        return Book.builder()
                .price(document.getPrice())
                .authors(authors)
                .translators(translators)
                .build();
    }

    // 국립중앙도서관
    public static Book of(Doc doc) {
        return Book.builder()
                .subject(doc.getSubject())
                .build();
    }

    // 네이버
    public static Book of(Item item) {
        return Book.builder()
                .id(item.getIsbn())
                .title(item.getTitle())
                .publisher(item.getPublisher())
                .image(item.getImage())
                .introduce(item.getDescription())
                .build();
    }
}
