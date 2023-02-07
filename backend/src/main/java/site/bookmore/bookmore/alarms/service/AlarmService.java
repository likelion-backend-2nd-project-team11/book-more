package site.bookmore.bookmore.alarms.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import site.bookmore.bookmore.alarms.dto.AlarmResponse;
import site.bookmore.bookmore.alarms.repository.AlarmRepository;
import site.bookmore.bookmore.books.entity.Book;
import site.bookmore.bookmore.common.exception.not_found.ReviewNotFoundException;
import site.bookmore.bookmore.common.exception.not_found.UserNotFoundException;
import site.bookmore.bookmore.reviews.repository.ReviewRepository;
import site.bookmore.bookmore.users.entity.User;
import site.bookmore.bookmore.users.repositroy.UserRepository;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AlarmService {
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final AlarmRepository alarmRepository;

    /**
     * 나의 알림 조회
     */
    public Page<AlarmResponse> findByFollowingReview(Pageable pageable, String email) {
        User target = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        return alarmRepository.findByTargetUser(target, pageable).map(alarm -> {
            Map<String, Object> source = new HashMap<>();
            switch (alarm.getAlarmType()) {
                case NEW_FOLLOW_REVIEW:
                case NEW_LIKE_ON_REVIEW:
                    Long reviewId = alarm.getSource();
                    Book book = reviewRepository.findById(reviewId)
                            .orElseThrow(ReviewNotFoundException::new)
                            .getBook();

                    source.put("reviewId", reviewId);
                    source.put("title", book.getTitle());

                default:
            }
            return AlarmResponse.of(alarm, source);
        });
    }

}
