package site.bookmore.bookmore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import site.bookmore.bookmore.books.util.crawler.KakaoBookCrawler;

@Configuration
public class BookCrawlerConfig {
    @Bean
    public KakaoBookCrawler kakaoBookCrawler() {
        final String pageSelector = "#tabContent > div:nth-child(1) > div.info_section.info_intro > div.wrap_cont > dl:nth-child(5) > dd";
        final String chapterSelector = "#tabContent > div:nth-child(1) > div:nth-child(5) > p";
        return new KakaoBookCrawler(pageSelector, chapterSelector);
    }
}
