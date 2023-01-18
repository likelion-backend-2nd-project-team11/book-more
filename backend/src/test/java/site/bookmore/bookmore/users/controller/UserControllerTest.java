package site.bookmore.bookmore.users.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import site.bookmore.bookmore.users.dto.UserJoinRequest;
import site.bookmore.bookmore.users.dto.UserJoinResponse;
import site.bookmore.bookmore.users.service.UserService;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@WithMockUser
class UserControllerTest {
    @MockBean
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    private LocalDate testDate = LocalDate.of(2022,01,01);

    @Autowired
    private MockMvc mockMvc;
    UserJoinRequest userJoinRequest = new UserJoinRequest("email@gmail.com", "password", "nickname", testDate);


    @Test
    @DisplayName("회원가입 - 성공")
    void join_success() throws Exception {

        UserJoinResponse userJoinResponse = new UserJoinResponse(0L, userJoinRequest.getEmail(), userJoinRequest.getNickname());

        given(userService.join(any(UserJoinRequest.class))).willReturn(userJoinResponse);

        mockMvc.perform(post("/api/v1/users/join")
                        .with(csrf())
                        .content(objectMapper.writeValueAsBytes(userJoinRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.result.id").value(0L))
                .andExpect(jsonPath("$.result.email").value("email@gmail.com"))
                .andExpect(jsonPath("$.result.nickname").value("nickname"))
                .andDo(print());
        verify(userService).join(any(UserJoinRequest.class));
    }

}