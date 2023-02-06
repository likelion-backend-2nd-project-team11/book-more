package site.bookmore.bookmore.users.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import site.bookmore.bookmore.reviews.repository.ReviewRepository;
import site.bookmore.bookmore.users.entity.Ranks;
import site.bookmore.bookmore.users.entity.User;
import site.bookmore.bookmore.users.repositroy.RanksRepository;
import site.bookmore.bookmore.users.repositroy.UserRepository;
import site.bookmore.bookmore.users.vo.RanksNativeVo;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class RanksScheduler {
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final RanksRepository ranksRepository;

    @Scheduled(cron = "${schedule.ranking.delaytime}")
    @Transactional
    public void scheduleRankTask() {
        log.info("랭킹 스케쥴러 시작");
        List<User> users = userRepository.findAll();

        // 유저 좋아요 수 정산
        for (User user : users) {
            Integer likeSum = reviewRepository.findSum(user.getId());
            if (likeSum == null) {
                likeSum = 0;
            }
            log.info("id : {}", user.getId());
            log.info("likeSum : {}", likeSum);

            Optional<Ranks> ranksOptional = ranksRepository.findByUser(user);
            if (ranksOptional.isPresent()) ranksOptional.get().updatePoint(likeSum);
        }

        // ranking 가져오기
        List<RanksNativeVo> rankList = ranksRepository.findAllRanking();

        // ranking 업데이트
        for (RanksNativeVo ranksNativeVo : rankList) {
            Long ranking = ranksNativeVo.getRanking();
            log.info("ranking : {}", ranking);
            ranksRepository.findById(ranksNativeVo.getId())
                    .ifPresent(ranks -> ranks.updateRanking(ranking));
        }

        log.info("랭킹 스케쥴러 완료");
    }
}
