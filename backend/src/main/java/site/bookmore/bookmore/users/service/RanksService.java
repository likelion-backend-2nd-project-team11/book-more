package site.bookmore.bookmore.users.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import site.bookmore.bookmore.common.exception.not_found.UserNotFoundException;
import site.bookmore.bookmore.common.exception.unauthorized.UserNotLoggedInException;
import site.bookmore.bookmore.users.dto.RanksResponse;
import site.bookmore.bookmore.users.entity.Ranks;
import site.bookmore.bookmore.users.entity.User;
import site.bookmore.bookmore.users.repositroy.RanksRepository;
import site.bookmore.bookmore.users.repositroy.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Builder
public class RanksService {
    private final RanksRepository ranksRepository;
    private final UserRepository userRepository;

    public List<RanksResponse> findTop100Ranks() {

        return ranksRepository.findTop100ByOrderByRankingAsc().stream().map(ranks -> new RanksResponse(ranks))
                .collect(Collectors.toList());
    }

    public RanksResponse findMyRanks(String email) {

        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        Ranks ranks = ranksRepository.findByUser(user).orElseThrow(UserNotLoggedInException::new);
        return new RanksResponse(ranks);
    }
}