package site.bookmore.bookmore.books.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.bookmore.bookmore.books.dto.BookResponse;
import site.bookmore.bookmore.books.service.BookService;
import site.bookmore.bookmore.books.util.api.kakao.dto.KakaoSearchParams;
import site.bookmore.bookmore.common.dto.ResultResponse;

@RestController
@RequiredArgsConstructor
@Api(tags = "5-도서 검색")
@RequestMapping("/api/v1/books")
public class BookController {
    private final BookService bookService;

    @ApiOperation(value = "리스트 검색")
    @GetMapping("")
    public ResultResponse<Page<BookResponse>> search(KakaoSearchParams kakaoSearchParams) {
        // 카카오 도서 검색 요청
        return ResultResponse.success(bookService.search(kakaoSearchParams));
    }

    @ApiOperation(value = "상세 검색")
    @GetMapping("/{isbn}")
    public ResultResponse<BookResponse> searchByISBN(@PathVariable String isbn) {
        return ResultResponse.success(bookService.searchByISBN(isbn));
    }
}
