package site.bookmore.bookmore.books.util.mapper;

import site.bookmore.bookmore.books.entity.Author;
import site.bookmore.bookmore.books.entity.Book;
import site.bookmore.bookmore.books.entity.Translator;
import site.bookmore.bookmore.books.util.api.kakao.dto.Document;
import site.bookmore.bookmore.books.util.api.kolis.dto.Doc;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class BookMapper {
    public static Book of(Document document) {
        Book book = Book.builder()
                .id(document.getISBN())
                .title(document.getTitle())
                .publisher(document.getPublisher())
                .image(document.getThumbnail())
                .introduce(document.getContents())
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
        Book book = Book.builder()
                .id(doc.getIsbn())
                .title(doc.getTitle())
                .subject(doc.getSubject())
                .publisher(doc.getPublisher())
                .image(doc.getImage_url())
                .pages(doc.getPage())
                .price(doc.getPrice())
                .build();

        Set<Author> authors = Arrays.stream(doc.getAuthor()
                        .replace(" ", "")
                        .split(","))
                .map(name -> Author.of(name, book))
                .collect(Collectors.toSet());

        book.addAuthors(authors);

        return book;
    }
}
