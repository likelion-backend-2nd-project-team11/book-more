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
    public static Book of(Document document) {
        Book book = Book.builder()
                .price(document.getPrice())
                .build();

        Set<Author> authors = document.getAuthors().stream()
                .map(name -> Author.of(name, book))
                .collect(Collectors.toSet());

        Set<Translator> translators = document.getTranslators().stream()
                .map(name -> Translator.of(name, book))
                .collect(Collectors.toSet());

        book.addAuthors(authors);
        book.addTranslators(translators);

        return book;
    }

    public static Book of(Doc doc) {
        return Book.builder()
                .subject(doc.getSubject())
                .build();
    }

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
