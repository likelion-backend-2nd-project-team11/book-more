package site.bookmore.bookmore.users.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import site.bookmore.bookmore.reviews.repository.ReviewRepository;
import site.bookmore.bookmore.users.entity.Ranks;
import site.bookmore.bookmore.users.entity.User;
import site.bookmore.bookmore.users.repositroy.RanksRepository;
import site.bookmore.bookmore.users.repositroy.UserRepository;
import site.bookmore.bookmore.users.vo.RanksNativeVo;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class RanksScheduler {
    private final UserRepository userRepository;
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

        for (RanksNativeVo ranksNativeVo : rankList) {
            Long ranking = ranksNativeVo.getRanking();
            log.info("ranking : {}", ranking);
            ranksRepository.save(Ranks.of(ranksNativeVo.getId(), ranksNativeVo.getPoint(), ranking, ranksNativeVo.getNickname()));
        }
    }
}
