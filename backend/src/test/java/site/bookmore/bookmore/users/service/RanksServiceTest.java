package site.bookmore.bookmore.users.service;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import site.bookmore.bookmore.users.dto.RanksResponse;
import site.bookmore.bookmore.users.entity.Ranks;
import site.bookmore.bookmore.users.entity.User;
import site.bookmore.bookmore.users.repositroy.RanksRepository;
import site.bookmore.bookmore.users.repositroy.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RanksServiceTest {
    @Mock
    private RanksRepository ranksRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    RanksService ranksService;

    private final User user = User.builder()
            .id(1L).email("test@test.com").nickname("test").password("1q2w3e4r")
            .birth(LocalDate.of(2002, 10, 5)).build();
    private final User user2 = User.builder()
            .id(2L).email("test2@test.com").nickname("test2").password("1q2w3e4r")
            .birth(LocalDate.of(2003, 10, 5)).build();

    @Test
    @DisplayName("랭킹 Top100 조회")
    void findTop100Ranks() {
        Ranks ranks1 = Ranks.builder().ranking(1L).id(1L).point(2).user(user).build();
        Ranks ranks2 = Ranks.builder().ranking(2L).id(2L).point(1).user(user2).build();

        List<Ranks> ranksList = List.of(ranks1, ranks2);

        when(ranksRepository.findTop100ByOrderByRankingAsc()).thenReturn(ranksList);

        List<RanksResponse> ranksResponseList = ranksService.findTop100Ranks();

        assertThat(ranksResponseList.size()).isEqualTo(2);

    }

    @Test
    @DisplayName("나의 랭킹 조회")
    void findMyRanks() {
        Ranks ranks1 = Ranks.builder().ranking(1L).id(1L).point(2).user(user).build();

        when(userRepository.findByEmailAndDeletedDatetimeIsNull(user.getEmail())).thenReturn(Optional.of(user));
        when(ranksRepository.findByUser(user)).thenReturn(Optional.of(ranks1));

        RanksResponse ranksResponse = ranksService.findMyRanks(user.getEmail());

        assertThat(ranksResponse.getId()).isEqualTo(1L);
    }
}