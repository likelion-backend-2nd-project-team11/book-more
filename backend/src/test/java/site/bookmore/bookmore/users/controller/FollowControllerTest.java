package site.bookmore.bookmore.users.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import site.bookmore.bookmore.common.exception.not_found.UserNotFoundException;
import site.bookmore.bookmore.users.service.FollowService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FollowController.class)
class FollowControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    FollowService followService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("팔로우 성공")
    @WithMockUser
    void follow_success() throws Exception {

        when(followService.following(any(), any())).thenReturn("팔로우 성공");

        mockMvc.perform(post("/api/v1/users/1/follow")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.result").exists());
    }

    @Test
    @DisplayName("팔로우 실패(1) - 로그인 하지 않은 경우")
    @WithAnonymousUser
    void follow_fail_1() throws Exception {

        when(followService.following(any(), any())).thenReturn("팔로우 성공");

        mockMvc.perform(post("/api/v1/users/1/follow")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("팔로우 실패(2) - 팔로우 할 대상이 없는 경우")
    @WithMockUser
    void follow_fail_2() throws Exception {

        when(followService.following(any(), any())).thenThrow(new UserNotFoundException());

        mockMvc.perform(post("/api/v1/users/1/follow")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.resultCode").value("ERROR"))
                .andExpect(jsonPath("$.result.errorCode").exists())
                .andExpect(jsonPath("$.result.message").exists());
    }

    @Test
    @DisplayName("언팔로우 성공")
    @WithMockUser
    void unfollow_success() throws Exception {

        when(followService.unfollowing(any(), any())).thenReturn("언팔로우 성공");

        mockMvc.perform(delete("/api/v1/users/1/follow")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.result").exists());
    }

    @Test
    @DisplayName("언팔로우 실패(1) - 로그인 하지 않은 경우")
    @WithAnonymousUser
    void unfollow_fail_1() throws Exception {

        when(followService.unfollowing(any(), any())).thenReturn("팔로우 성공");

        mockMvc.perform(delete("/api/v1/users/1/follow")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("언팔로우 실패(2) - 언팔로우 할 대상이 없는 경우")
    @WithMockUser
    void unfollow_fail_2() throws Exception {

        when(followService.unfollowing(any(), any())).thenThrow(new UserNotFoundException());

        mockMvc.perform(delete("/api/v1/users/1/follow")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.resultCode").value("ERROR"))
                .andExpect(jsonPath("$.result.errorCode").exists())
                .andExpect(jsonPath("$.result.message").exists());
    }
}