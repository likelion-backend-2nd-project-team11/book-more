package site.bookmore.bookmore.users.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import site.bookmore.bookmore.users.dto.RanksResponse;
import site.bookmore.bookmore.users.repositroy.RanksRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Builder
public class RanksService {
    private final RanksRepository ranksRepository;

    public List<RanksResponse> findTop100Ranks() {

        return ranksRepository.findTop100ByOrderByRankingAsc().stream().map(ranks -> new RanksResponse(ranks))
                .collect(Collectors.toList());
    }
}