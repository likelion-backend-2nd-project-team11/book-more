package site.bookmore.bookmore.reviews.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import site.bookmore.bookmore.reviews.dto.ChartRequest;
import site.bookmore.bookmore.reviews.dto.ReviewRequest;
import site.bookmore.bookmore.reviews.service.ReviewService;

import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        ReviewRequest reviewRequest = new ReviewRequest("body", false, ChartRequest.builder().build(), null);

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

    @Test
    @DisplayName("도서 리뷰 등록 실패 - 본문이 빈 값인 경우")
    @WithMockUser
    void create_body_is_null() throws Exception {
        // given
        ReviewRequest reviewRequest = new ReviewRequest(" ", false, ChartRequest.builder().build(), null);

        // then
        mockMvc.perform(post("/api/v1/books/9791158393083/reviews")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(reviewRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.resultCode").value("ERROR"))
                .andExpect(jsonPath("$.result.errorCode").value("body 입력값 오류"))
                .andExpect(jsonPath("$.result.message").value("본문은 반드시 작성되어야 하는 항목입니다."));
    }

    @Test
    @DisplayName("도서 리뷰 등록 실패 - chart 잘못된 입력")
    @WithMockUser
    void create_chart_fault() throws Exception {
        // given
        ReviewRequest reviewRequest = new ReviewRequest("body", false,
                ChartRequest.builder()
                        .professionalism(0)
                        .fun(5)
                        .readability(5)
                        .collectible(5)
                        .difficulty(5)
                        .build(),
                null);

        // then
        mockMvc.perform(post("/api/v1/books/9791158393083/reviews")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(reviewRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.resultCode").value("ERROR"))
                .andExpect(jsonPath("$.result.errorCode").value("professionalism 입력값 오류"))
                .andExpect(jsonPath("$.result.message").value("1 ~ 5점 사이의 점수만 부여할 수 있습니다."));
    }

    /* ========== 리뷰, 태그 등록 =========*/
    @Test
    @DisplayName("도서 리뷰 등록 성공 - 태그가 있는 경우")
    @WithMockUser
    void create_with_tag_success() throws Exception {
        final Set<String> tags = Set.of("tag1", "tag2");
        // given
        ReviewRequest reviewRequest = new ReviewRequest("body", false, ChartRequest.builder().build(), tags);

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

    /* ========== 도서 리뷰 조회 ========== */
    @Test
    @DisplayName("도서 리뷰 조회 성공")
    @WithMockUser
    void read_success() throws Exception {
        // when
        when(reviewService.read(any(), eq("9791158393083")))
                .thenReturn(Page.empty());

        // then
        mockMvc.perform(get("/api/v1/books/9791158393083/reviews")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.result").exists());

        verify(reviewService).read(any(), eq("9791158393083"));
    }

    /* ========== 도서 리뷰 수정 ========== */
    @Test
    @DisplayName("도서 리뷰 수정 성공")
    @WithMockUser
    void update_success() throws Exception {
        // given
        ReviewRequest reviewRequest = new ReviewRequest("new body", true, ChartRequest.builder().build(), null);

        // when
        when(reviewService.update(any(ReviewRequest.class), eq(1L), anyString()))
                .thenReturn(1L);

        // then
        mockMvc.perform(patch("/api/v1/books//reviews/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(reviewRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.result.id").value(1))
                .andExpect(jsonPath("$.result.message").value("리뷰 수정 완료"));

        verify(reviewService).update(any(ReviewRequest.class), eq(1L), anyString());
    }

    /* ========== 도서 리뷰 삭제 ========== */
    @Test
    @DisplayName("도서 리뷰 삭제 성공")
    @WithMockUser
    void delete_success() throws Exception {
        // when
        when(reviewService.delete(eq(1L), anyString()))
                .thenReturn(1L);

        // then
        mockMvc.perform(delete("/api/v1/books//reviews/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.result.id").value(1))
                .andExpect(jsonPath("$.result.message").value("리뷰 삭제 완료"));

        verify(reviewService).delete(eq(1L), anyString());
    }

    /* ========== 도서 리뷰 좋아요 | 취소 ========== */
    @Test
    @DisplayName("도서 리뷰 좋아요 성공")
    @WithMockUser
    void doLikes_success() throws Exception {
        // when
        when(reviewService.doLikes(anyString(), eq(1L)))
                .thenReturn(true);

        // then
        mockMvc.perform(post("/api/v1/books/reviews/1/likes")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.result").value("좋아요를 눌렀습니다."));

        verify(reviewService).doLikes(anyString(), eq(1L));
    }

    @Test
    @DisplayName("도서 리뷰 좋아요 취소 성공")
    @WithMockUser
    void doLikes_cancel_success() throws Exception {
        // when
        when(reviewService.doLikes(anyString(), eq(1L)))
                .thenReturn(false);

        // then
        mockMvc.perform(post("/api/v1/books/reviews/1/likes")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.result").value("좋아요가 취소되었습니다."));

        verify(reviewService).doLikes(anyString(), eq(1L));
    }
}