package site.bookmore.bookmore.books.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import reactor.core.publisher.Mono;
import site.bookmore.bookmore.books.util.api.kakao.KakaoBookSearch;
import site.bookmore.bookmore.books.util.api.kakao.dto.Document;
import site.bookmore.bookmore.books.util.api.kakao.dto.KakaoSearchParams;
import site.bookmore.bookmore.books.util.api.kakao.dto.KakaoSearchResponse;
import site.bookmore.bookmore.books.util.api.kakao.dto.Meta;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KakaoBookSearch kakaoBookSearch;

    private static final String SUCCESS = "SUCCESS";

    @Test
    @DisplayName("도서 리스트 검색 정상")
    @WithMockUser
    void search() throws Exception {
        //given
        List<Document> documents = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Document ele = Document.builder()
                    .title("title" + i)
                    .authors(List.of("authors"))
                    .translators(List.of("translator"))
                    .contents("contents" + 1)
                    .isbn(String.valueOf(i))
                    .price(10000)
                    .url("http://test.com/" + i)
                    .build();
            documents.add(ele);
        }
        Meta meta = Meta.builder()
                .pageable_count(18)
                .total_count(20)
                .is_end(false)
                .build();

        KakaoSearchResponse kakaoSearchResponse = new KakaoSearchResponse(meta, documents);

        given(kakaoBookSearch.search(any(KakaoSearchParams.class))).willReturn(Mono.just(kakaoSearchResponse));

        mockMvc.perform(get("/api/v1/books?query=정의란 무엇인가")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value(SUCCESS))
                .andExpect(jsonPath("$.result.content").isArray())
                .andExpect(jsonPath("$.result.totalElements").value(meta.getPageable_count()));

        verify(kakaoBookSearch).search(any(KakaoSearchParams.class));
    }

    @Test
    @DisplayName("도서 리스트 검색 - 검색 결과 없음")
    @WithMockUser
    void search_documents_empty() throws Exception {
        //given
        List<Document> documents = new ArrayList<>();
        Meta meta = Meta.builder()
                .pageable_count(0)
                .total_count(0)
                .is_end(true)
                .build();

        KakaoSearchResponse kakaoSearchResponse = new KakaoSearchResponse(meta, documents);

        given(kakaoBookSearch.search(any(KakaoSearchParams.class))).willReturn(Mono.just(kakaoSearchResponse));

        mockMvc.perform(get("/api/v1/books?query=책이름")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value(SUCCESS))
                .andExpect(jsonPath("$.result.content").isArray())
                .andExpect(jsonPath("$.result.totalElements").value(meta.getPageable_count()));

        verify(kakaoBookSearch).search(any(KakaoSearchParams.class));
    }
}