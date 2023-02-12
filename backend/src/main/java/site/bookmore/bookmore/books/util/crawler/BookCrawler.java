package site.bookmore.bookmore.books.util.crawler;

import site.bookmore.bookmore.books.entity.Book;

public interface BookCrawler {
    Book execute(String url);
    Integer parsePage(String originPage);
    String parseChapter(String originChapter);
}
