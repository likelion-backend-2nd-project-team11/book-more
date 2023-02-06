package site.bookmore.bookmore.users.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import site.bookmore.bookmore.reviews.repository.LikesRepository;
import site.bookmore.bookmore.reviews.repository.ReviewRepository;
import site.bookmore.bookmore.users.dto.RanksResponse;
import site.bookmore.bookmore.users.entity.Ranks;
import site.bookmore.bookmore.users.entity.User;
import site.bookmore.bookmore.users.repositroy.RanksRepository;
import site.bookmore.bookmore.users.repositroy.UserRepository;
import site.bookmore.bookmore.users.vo.RanksNativeVo;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Builder
@Component
public class RanksService {
    private final UserRepository userRepository;
    private final LikesRepository likesRepository;
    private final ReviewRepository reviewRepository;
    private final RanksRepository ranksRepository;

    @Scheduled(cron = "${schedule.ranking.delaytime}")
    public void scheduleRankTask() {

        List<User> users = userRepository.findAll();

        for (User user : users) {
            Integer likeSum = reviewRepository.findSum(user.getId());
            if (likeSum == null) {
                likeSum = 0;
            }
            log.info("id : {}", user.getId());
            log.info("likeSum : {}", likeSum);
            ranksRepository.save(new Ranks(user.getId(), likeSum));
        }

        // ranking 가져오기
        List<RanksNativeVo> rankList = ranksRepository.findAllRanking();

        for (int i = 0; i < rankList.size(); i++) {
            Long ranking = rankList.get(i).getRanking();
            log.info("ranking : {}", ranking);
            ranksRepository.save(Ranks.of(rankList.get(i).getId(), rankList.get(i).getPoint(), ranking, rankList.get(i).getNickname()));
        }
    }

    public List<RanksResponse> findTop100Ranks() {

        return ranksRepository.findTop100ByOrderByRankingAsc().stream().map(ranks -> new RanksResponse(ranks))
                .collect(Collectors.toList());
    }
}