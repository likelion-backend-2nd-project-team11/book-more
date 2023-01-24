package site.bookmore.bookmore.books.util.mapper;

import site.bookmore.bookmore.books.dto.BookResponse;
import site.bookmore.bookmore.books.entity.Author;
import site.bookmore.bookmore.books.entity.Book;
import site.bookmore.bookmore.books.entity.Translator;
import site.bookmore.bookmore.books.util.api.kakao.dto.Document;

import java.util.stream.Collectors;

public class BookResponseMapper {
    public static BookResponse of(Document kakaoDocument) {
        return BookResponse.builder()
                .isbn(kakaoDocument.getISBN())
                .title(kakaoDocument.getTitle())
                .authors(kakaoDocument.getAuthors())
                .translators(kakaoDocument.getTranslators())
                .publisher(kakaoDocument.getPublisher())
                .image(kakaoDocument.getThumbnail())
                .price(kakaoDocument.getPrice())
                .build();
    }

    public static BookResponse of(Book book) {
        return BookResponse.builder()
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
