package site.bookmore.bookmore.books.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.bookmore.bookmore.books.dto.BookResponse;
import site.bookmore.bookmore.books.util.api.kakao.KakaoBookSearch;
import site.bookmore.bookmore.books.util.api.kakao.dto.KakaoSearchParams;
import site.bookmore.bookmore.books.util.api.kakao.dto.KakaoSearchResponse;
import site.bookmore.bookmore.common.dto.ResultResponse;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/books")
public class BookController {
    private final KakaoBookSearch kakaoBookSearch;

    // Todo. 카카오 api 의존도 해결
    @GetMapping("")
    public ResultResponse<Page<BookResponse>> search(KakaoSearchParams kakaoSearchParams) {
        // 카카오 도서 검색 요청
        KakaoSearchResponse response = kakaoBookSearch.search(kakaoSearchParams).block();
        List<BookResponse> bookResponses = response.getDocuments().stream().map(BookResponse::of).collect(Collectors.toList());
        Page<BookResponse> result = new PageImpl<>(bookResponses, Pageable.unpaged(), response.getMeta().getPageable_count());
        return ResultResponse.success(result);
    }
}
