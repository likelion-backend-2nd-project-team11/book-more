package site.bookmore.bookmore.books.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import site.bookmore.bookmore.books.dto.BookDetailResponse;
import site.bookmore.bookmore.books.dto.BookResponse;
import site.bookmore.bookmore.books.dto.BookSearchParams;
import site.bookmore.bookmore.books.entity.Subject;
import site.bookmore.bookmore.books.service.BookService;

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
    private BookService bookService;

    private static final String SUCCESS = "SUCCESS";

    @Test
    @DisplayName("도서 리스트 검색 정상")
    @WithMockUser
    void search() throws Exception {
        //given
        int SIZE = 10;
        List<BookResponse> bookResponses = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            BookResponse ele = BookResponse.builder()
                    .title("title" + i)
                    .isbn(String.valueOf(i))
                    .image("http://test.com/" + i)
                    .build();
            bookResponses.add(ele);
        }

        given(bookService.search(any(BookSearchParams.class))).willReturn(new PageImpl<>(bookResponses, Pageable.unpaged(), SIZE));

        mockMvc.perform(get("/api/v1/books?query=정의란 무엇인가")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value(SUCCESS))
                .andExpect(jsonPath("$.result.content").isArray())
                .andExpect(jsonPath("$.result.totalElements").exists());

        verify(bookService).search(any(BookSearchParams.class));
    }

    @Test
    @DisplayName("도서 리스트 검색 - 검색 결과 없음")
    @WithMockUser
    void search_documents_empty() throws Exception {
        given(bookService.search(any(BookSearchParams.class))).willReturn(new PageImpl<>(new ArrayList<>()));

        mockMvc.perform(get("/api/v1/books?query=책이름")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value(SUCCESS))
                .andExpect(jsonPath("$.result.content").isArray())
                .andExpect(jsonPath("$.result.totalElements").exists());

        verify(bookService).search(any(BookSearchParams.class));
    }

    @Test
    @DisplayName("도서 상세 검색 정상")
    @WithMockUser
    void searchByISBN() throws Exception {
        BookDetailResponse bookDetailResponse = BookDetailResponse.builder()
                                                                    .isbn("10001")
                                                                    .title("title")
                                                                    .subject(Subject.예술)
                                                                    .publisher("publisher")
                                                                    .pages(100)
                                                                    .image("http://test.com/img/111.jpg")
                                                                    .introduce("intro")
                                                                    .price(10000)
                                                                    .build();

        given(bookService.searchByISBN("10001")).willReturn(bookDetailResponse);

        mockMvc.perform(get("/api/v1/books/10001")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value(SUCCESS))
                .andExpect(jsonPath("$.result.isbn").value(bookDetailResponse.getIsbn()))
                .andExpect(jsonPath("$.result.title").value(bookDetailResponse.getTitle()))
                .andExpect(jsonPath("$.result.subject").value(bookDetailResponse.getSubject().name()))
                .andExpect(jsonPath("$.result.publisher").value(bookDetailResponse.getPublisher()))
                .andExpect(jsonPath("$.result.pages").value(bookDetailResponse.getPages()))
                .andExpect(jsonPath("$.result.image").value(bookDetailResponse.getImage()))
                .andExpect(jsonPath("$.result.introduce").value(bookDetailResponse.getIntroduce()))
                .andExpect(jsonPath("$.result.price").value(bookDetailResponse.getPrice()));

        verify(bookService).searchByISBN("10001");
    }
}