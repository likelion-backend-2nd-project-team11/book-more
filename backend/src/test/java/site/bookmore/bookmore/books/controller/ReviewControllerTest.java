package site.bookmore.bookmore.books.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import site.bookmore.bookmore.books.dto.ReviewRequest;
import site.bookmore.bookmore.books.service.ReviewService;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReviewController.class)
class ReviewControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ReviewService reviewService;

    @Autowired
    ObjectMapper objectMapper;

    /* ========== 도서 리뷰 등록 ========== */
    @Test
    @DisplayName("도서 리뷰 등록 성공")
    @WithMockUser
    void create_success() throws Exception {
        // given
        ReviewRequest reviewRequest = new ReviewRequest("body", false, 5, 5, 5, 5, 5);

        // when
        when(reviewService.create(any(ReviewRequest.class), eq("9791158393083"), anyString()))
                .thenReturn(1L);

        // then
        mockMvc.perform(post("/api/v1/books/9791158393083/reviews")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(reviewRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.result.id").value(1))
                .andExpect(jsonPath("$.result.message").value("리뷰 등록 완료"));

        verify(reviewService).create(any(ReviewRequest.class), eq("9791158393083"), anyString());
    }
}