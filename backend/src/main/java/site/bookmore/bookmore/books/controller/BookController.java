package site.bookmore.bookmore.books.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.bookmore.bookmore.books.dto.BookDetailResponse;
import site.bookmore.bookmore.books.dto.BookResponse;
import site.bookmore.bookmore.books.dto.BookSearchParams;
import site.bookmore.bookmore.books.service.BookService;
import site.bookmore.bookmore.common.dto.ResultResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/books")
public class BookController {
    private final BookService bookService;

    @GetMapping("")
    public ResultResponse<Page<BookResponse>> search(BookSearchParams bookSearchParams) {
        log.info("도서 검색 요청 query : {}", bookSearchParams.getQuery());
        return ResultResponse.success(bookService.search(bookSearchParams));
    }

    @GetMapping("/{isbn}")
    public ResultResponse<BookDetailResponse> searchByISBN(@PathVariable String isbn) {
        log.info("도서 상세 조회 요청 ISBN : {}", isbn);
        return ResultResponse.success(bookService.searchByISBN(isbn));
    }
}
