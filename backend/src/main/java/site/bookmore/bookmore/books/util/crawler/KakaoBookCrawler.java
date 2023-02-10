package site.bookmore.bookmore.books.util.crawler;

import static site.bookmore.bookmore.books.entity.Book.CHAPTER_LENGTH;

public class KakaoBookCrawler extends AbstractBookCrawler {
    public KakaoBookCrawler(String pageSelector, String chapterSelector) {
        super(pageSelector, chapterSelector);
    }

    @Override
    public Integer parsePage(String originPage) {
        originPage =  originPage.replaceAll(" ", "").split("\\|")[0];
        try {
            return Integer.parseInt(originPage);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String parseChapter(String originChapter) {
        if (originChapter.length() > CHAPTER_LENGTH) return originChapter.substring(0, CHAPTER_LENGTH);
        if (originChapter.length() == 0) return null;
        return originChapter;
    }
}
