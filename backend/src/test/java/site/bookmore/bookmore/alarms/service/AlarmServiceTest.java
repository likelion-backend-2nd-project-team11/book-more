package site.bookmore.bookmore.alarms.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import site.bookmore.bookmore.alarms.dto.AlarmResponse;
import site.bookmore.bookmore.alarms.entity.Alarm;
import site.bookmore.bookmore.alarms.entity.AlarmType;
import site.bookmore.bookmore.alarms.repository.AlarmRepository;
import site.bookmore.bookmore.books.entity.Book;
import site.bookmore.bookmore.common.exception.ErrorCode;
import site.bookmore.bookmore.common.exception.conflict.DuplicateConfirmedException;
import site.bookmore.bookmore.common.exception.forbidden.InvalidPermissionException;
import site.bookmore.bookmore.reviews.entity.Review;
import site.bookmore.bookmore.reviews.repository.ReviewRepository;
import site.bookmore.bookmore.users.entity.User;
import site.bookmore.bookmore.users.repositroy.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.when;


@ExtendWith(MockitoExtension.class)
class AlarmServiceTest {

    @Mock
    private AlarmRepository alarmRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    AlarmService alarmService;
    Pageable pageable = PageRequest.of(0, 20);

    private final User user = User.builder()
            .id(1L).email("test@test.com").nickname("test").password("1q2w3e4r")
            .birth(LocalDate.of(2002, 10, 5)).build();
    private final User targetUser = User.builder()
            .id(2L).email("test2@test.com").nickname("test2").password("1q2w3e4r")
            .birth(LocalDate.of(2002, 9, 5)).build();
    private final Alarm alarm1 = Alarm.builder().id(1L).alarmType(AlarmType.NEW_FOLLOW).confirmed(false).fromUser(user).targetUser(targetUser)
            .source(2L).build();
    private final Alarm alarm2 = Alarm.builder().id(2L).alarmType(AlarmType.NEW_FOLLOW_REVIEW).confirmed(true).fromUser(user).targetUser(targetUser)
            .source(1L).build();


    @Test
    @DisplayName("나의 모든 알람 조회")
    void findAllMyAlarm() {

        Book book = Book.builder().id("10001").title("title1").publisher("publisher1").price(10000).build();
        Review review = Review.builder().id(1L).author(user).book(book).build();

        Page<Alarm> alarmPage = new PageImpl<>(List.of(alarm1, alarm2));

        when(userRepository.findByEmailAndDeletedDatetimeIsNull(user.getEmail())).thenReturn(Optional.of(user));
        when(alarmRepository.findByTargetUserAndConfirmedIsFalseAndDeletedDatetimeIsNull(user, pageable)).thenReturn(alarmPage);
        when(reviewRepository.findById(review.getId())).thenReturn(Optional.of(review));

        Page<AlarmResponse> responses = alarmService.findByFollowingReview(pageable, user.getEmail());

        assertThat(responses.getTotalElements()).isEqualTo(2);
    }

    @Test
    @DisplayName("나의 새로운 알람 조회")
    void findNewMyAlarm() {

        Book book = Book.builder().id("10001").title("title1").publisher("publisher1").price(10000).build();
        Review review = Review.builder().id(1L).author(user).book(book).build();

        Page<Alarm> alarmPage = new PageImpl<>(List.of(alarm1, alarm2));

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(alarmRepository.findByTargetUserAndConfirmedIsFalseAndDeletedDatetimeIsNull(user, pageable)).thenReturn(alarmPage);
        when(reviewRepository.findById(review.getId())).thenReturn(Optional.of(review));


        Page<AlarmResponse> responses = alarmService.getNewAlarms(pageable, user.getEmail());

        assertThat(responses.getTotalElements()).isEqualTo(2);
    }

    @Test
    @DisplayName("알람 확인")
    void doConfirm() {
        when(userRepository.findByEmail(targetUser.getEmail())).thenReturn(Optional.of(targetUser));
        when(alarmRepository.findById(alarm1.getId())).thenReturn(Optional.of(alarm1));

        alarm2.isConfirmed();
        assertThat(alarmService.doConfirm(targetUser.getEmail(), alarm1.getId())).isEqualTo("알림이 읽음 처리되었습니다.");
    }

    @Test
    @DisplayName("알람 확인 실패 - 이미 확인된 알림")
    void doConfirm_fail() {

        when(userRepository.findByEmail(targetUser.getEmail())).thenReturn(Optional.of(targetUser));
        when(alarmRepository.findById(alarm2.getId())).thenReturn(Optional.of(alarm2));

        DuplicateConfirmedException exception = Assertions.assertThrows(DuplicateConfirmedException.class, () -> {
            alarmService.doConfirm(targetUser.getEmail(), alarm2.getId());
        });

        assertEquals(ErrorCode.DUPLICATED_CONFIRMED, exception.getErrorCode());
    }

    @Test
    @DisplayName("알람 확인 실패 - 권한 없음")
    void doConfirm_fail2() {

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(alarmRepository.findById(alarm1.getId())).thenReturn(Optional.of(alarm1));

        InvalidPermissionException exception = Assertions.assertThrows(InvalidPermissionException.class, () -> {
            alarmService.doConfirm(user.getEmail(), alarm1.getId());
        });

        assertEquals(ErrorCode.INVALID_PERMISSION, exception.getErrorCode());
    }
}