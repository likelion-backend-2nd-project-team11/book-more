package site.bookmore.bookmore.alarms.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.bookmore.bookmore.alarms.dto.AlarmResponse;
import site.bookmore.bookmore.alarms.entity.Alarm;
import site.bookmore.bookmore.alarms.repository.AlarmRepository;
import site.bookmore.bookmore.books.entity.Book;
import site.bookmore.bookmore.common.exception.conflict.DuplicateConfirmedException;
import site.bookmore.bookmore.common.exception.forbidden.InvalidPermissionException;
import site.bookmore.bookmore.common.exception.not_found.AlarmNotFoundException;
import site.bookmore.bookmore.common.exception.not_found.ReviewNotFoundException;
import site.bookmore.bookmore.common.exception.not_found.UserNotFoundException;
import site.bookmore.bookmore.reviews.repository.ReviewRepository;
import site.bookmore.bookmore.users.entity.User;
import site.bookmore.bookmore.users.repositroy.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class AlarmService {
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final AlarmRepository alarmRepository;

    /**
     * 나의 모든 알림 조회
     */
    public Page<AlarmResponse> findByFollowingReview(Pageable pageable, String email) {
        User target = userRepository.findByEmailAndDeletedDatetimeIsNull(email).orElseThrow(UserNotFoundException::new);
        return alarmRepository.findByTargetUserAndDeletedDatetimeIsNull(target, pageable).map(getAlarmResponse());
    }

    /**
     * 나의 새로운 알림 조회
     */
    public Page<AlarmResponse> getNewAlarms(Pageable pageable, String email) {
        User target = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        return alarmRepository.findByTargetUserAndConfirmedIsFalseAndDeletedDatetimeIsNull(target, pageable).map(getAlarmResponse());
    }

    @Transactional
    public String doConfirm(String email, Long alarmId) {
        User target = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        Alarm alarm = alarmRepository.findById(alarmId).orElseThrow(AlarmNotFoundException::new);

        if (!Objects.equals(target.getEmail(), alarm.getTargetUser().getEmail())) {
            throw new InvalidPermissionException();
        }

        if (Objects.equals(alarm.isConfirmed(), true)) {
            throw new DuplicateConfirmedException();
        }

        alarm.confirm();
        return "알림이 읽음 처리되었습니다.";
    }

    private Function<Alarm, AlarmResponse> getAlarmResponse() {
        return alarm -> {
            Map<String, Object> source = new HashMap<>();
            switch (alarm.getAlarmType()) {
                case NEW_FOLLOW_REVIEW:
                case NEW_LIKE_ON_REVIEW:
                    Long reviewId = alarm.getSource();
                    Book book = reviewRepository.findById(reviewId)
                            .orElseThrow(ReviewNotFoundException::new)
                            .getBook();

                    source.put("isbn", book.getId());
                    source.put("title", book.getTitle());

                default:
            }
            return AlarmResponse.of(alarm, source);
        };
    }
}