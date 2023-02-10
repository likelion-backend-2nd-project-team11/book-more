package site.bookmore.bookmore.books.util.crawler;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import site.bookmore.bookmore.books.entity.Book;

import java.io.IOException;

@Slf4j
public abstract class AbstractBookCrawler implements BookCrawler{
    private final String pageSelector;
    private final String chapterSelector;

    public AbstractBookCrawler(String pageSelector, String chapterSelector) {
        this.pageSelector = pageSelector;
        this.chapterSelector = chapterSelector;
    }

    public Book execute(String url) {
        Book book = Book.empty();
        try {
            log.info("크롤링 시작");
            Connection conn = Jsoup.connect(url);

            Document html = conn.get();

            Elements files = html.select(pageSelector);
            String originPage = files.text();
            Integer page = parsePage(originPage);

            Elements files2 = html.select(chapterSelector);
            String originChapter = files2.html();
            String content = parseChapter(originChapter);

            book = Book.builder()
                    .pages(page)
                    .chapter(content)
                    .build();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            log.info("크롤링 종료");
        }
        return book;
    }

    public abstract Integer parsePage(String originPage);
    public abstract String parseChapter(String originChapter);
}
