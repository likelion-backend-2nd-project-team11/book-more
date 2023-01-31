package site.bookmore.bookmore.books.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.bookmore.bookmore.books.dto.BookDetailResponse;
import site.bookmore.bookmore.books.dto.BookResponse;
import site.bookmore.bookmore.books.dto.BookSearchParams;
import site.bookmore.bookmore.books.service.BookService;
import site.bookmore.bookmore.common.dto.ResultResponse;

import javax.validation.Valid;
import javax.validation.constraints.Size;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@Api(tags = "5-도서 검색")
@RequestMapping("/api/v1/books")
public class BookController {
    private final BookService bookService;

    @ApiOperation(value = "리스트 검색")
    @GetMapping("")
    public ResultResponse<Page<BookResponse>> search(@Valid BookSearchParams bookSearchParams) {
        log.info("도서 검색 요청 query : {}", bookSearchParams.getQuery());
        return ResultResponse.success(bookService.search(bookSearchParams));
    }

    @ApiOperation(value = "상세 검색")
    @GetMapping("/{isbn}")
    public ResultResponse<BookDetailResponse> searchByISBN(
            @PathVariable("isbn")
            @Size(min = 13, max = 13, message = "13자리의 isbn으로 검색해주세요.")
            String isbn
    ) {
        log.info("도서 상세 조회 요청 ISBN : {}", isbn);
        return ResultResponse.success(bookService.searchByISBN(isbn));
    }
}
