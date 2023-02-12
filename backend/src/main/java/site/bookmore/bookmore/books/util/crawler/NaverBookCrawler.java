package site.bookmore.bookmore.books.util.crawler;

import static site.bookmore.bookmore.books.entity.Book.CHAPTER_LENGTH;

public class NaverBookCrawler extends AbstractBookCrawler{
    public NaverBookCrawler(String pageSelector, String chapterSelector) {
        super(pageSelector, chapterSelector);
    }

    @Override
    public Integer parsePage(String originPage) {
        try {
            return Integer.parseInt(originPage.replaceAll("쪽", ""));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String parseChapter(String originChapter) {
        String content = originChapter.replaceAll("<b>", "<strong>")
                .replaceAll("</b>", "</strong>");

        if (content.length() > CHAPTER_LENGTH) return content.substring(0, CHAPTER_LENGTH);
        if (content.length() == 0) return null;
        return content;
    }
}
