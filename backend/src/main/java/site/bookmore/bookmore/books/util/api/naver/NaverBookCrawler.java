package site.bookmore.bookmore.books.util.api.naver;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import site.bookmore.bookmore.books.entity.Book;

import java.io.IOException;

import static site.bookmore.bookmore.books.entity.Book.CHAPTER_LENGTH;

@Slf4j
@Component
public class NaverBookCrawler {
    public static Book execute(String url) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Book book = Book.empty();
        try {
            Connection conn = Jsoup.connect(url);

            Document html = conn.get();

            Elements files = html.select(".bookBasicInfo_info_detail__I0Fx5 > span:nth-child(1)");
            String originPage = files.text();
            Integer page;
            try {
                page = Integer.parseInt(originPage.replaceAll("쪽", ""));
            } catch (NumberFormatException e) {
                log.error("페이지 파싱 실패 origin: {}", originPage);
                page = null;
            }

            Elements files2 = html.select("#book_section-info > div:nth-child(5) > div.infoItem_data_box__VwBf3 > div.infoItem_data_text__bUgVI");
            String originContent = files2.html();

            String content = originContent.replaceAll("<b>", "<strong>")
                    .replaceAll("</b>", "</strong>");

            if (content.length() > CHAPTER_LENGTH) content = content.substring(0, CHAPTER_LENGTH);

            book = Book.builder()
                    .pages(page)
                    .chapter(content)
                    .build();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            stopWatch.stop();
            log.info("네이버 도서 크롤링 완료 [{}ms]", stopWatch.getTotalTimeMillis());
        }
        return book;
    }
}
