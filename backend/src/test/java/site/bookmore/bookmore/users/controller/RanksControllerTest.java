package site.bookmore.bookmore.users.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import site.bookmore.bookmore.users.dto.RanksResponse;
import site.bookmore.bookmore.users.service.RanksService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RanksController.class)
class RanksControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    RanksService ranksService;



    @Test
    @DisplayName("Ranks Test")
    @WithMockUser(username = "user2")
    void alarm_following_list() throws Exception {
        List<RanksResponse> response = List.of(RanksResponse.builder()
                .id(1L)
                .point(2)
                .ranking(2L)
                .build());
        Page<RanksResponse> responsePage = new PageImpl<>(response);

        given(ranksService.findRanks(any(Pageable.class), eq("user2"))).willReturn(responsePage);

        mockMvc.perform(get("/api/v1/users/ranks")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..['id']").exists())
                .andExpect(jsonPath("$..['point']").exists())
                .andExpect(jsonPath("$..['ranking']").exists());

        verify(ranksService).findRanks(any(Pageable.class), eq("user2"));
    }
}