package site.bookmore.bookmore.users.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import site.bookmore.bookmore.users.dto.RanksResponse;
import site.bookmore.bookmore.users.service.RanksService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
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
    @DisplayName("랭킹 조회")
    @WithMockUser
    void ranks_list_success() throws Exception {


        List<RanksResponse> ranksList = List.of(
                RanksResponse.builder().id(1L).point(4).ranking(2L).build(),
                RanksResponse.builder().id(2L).point(3).ranking(3L).build(),
                RanksResponse.builder().id(3L).point(7).ranking(1L).build());
        given(ranksService.findTop100Ranks()).willReturn(ranksList);

        mockMvc.perform(get("/api/v1/users/ranks")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..['id']").exists())
                .andExpect(jsonPath("$..['point']").exists())
                .andExpect(jsonPath("$..['ranking']").exists());

    }

    @Test
    @DisplayName("개인 랭킹 조회")
    @WithMockUser
    void my_ranks_success() throws Exception {

        RanksResponse response = RanksResponse.builder()
                .id(1L)
                .point(3)
                .nickName("nickname")
                .ranking(1L)
                .build();
        given(ranksService.findMyRanks(any())).willReturn(response);

        mockMvc.perform(get("/api/v1/users/ranks/my")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..['id']").exists())
                .andExpect(jsonPath("$..['nickName']").exists())
                .andExpect(jsonPath("$..['point']").exists())
                .andExpect(jsonPath("$..['ranking']").exists());

    }
}
