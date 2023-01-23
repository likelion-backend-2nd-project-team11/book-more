package site.bookmore.bookmore.books.controller;

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
@RequestMapping("/api/v1/books")
public class BookController {
    private final BookService bookService;

    @GetMapping("")
    public ResultResponse<Page<BookResponse>> search(KakaoSearchParams kakaoSearchParams) {
        // 카카오 도서 검색 요청
        return ResultResponse.success(bookService.searchAtKakao(kakaoSearchParams));
    }

    @GetMapping("/{isbn}")
    public ResultResponse<BookResponse> searchByISBN(@PathVariable String isbn) {
        return ResultResponse.success(bookService.searchByISBN(isbn));
    }
}
