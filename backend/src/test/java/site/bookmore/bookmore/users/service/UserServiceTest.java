package site.bookmore.bookmore.users.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import site.bookmore.bookmore.alarms.repository.AlarmRepository;
import site.bookmore.bookmore.common.exception.AbstractAppException;
import site.bookmore.bookmore.common.exception.ErrorCode;
import site.bookmore.bookmore.common.exception.conflict.DuplicateEmailException;
import site.bookmore.bookmore.common.exception.conflict.DuplicateNicknameException;
import site.bookmore.bookmore.common.exception.not_found.UserNotFoundException;
import site.bookmore.bookmore.common.exception.unauthorized.InvalidPasswordException;
import site.bookmore.bookmore.common.exception.unauthorized.InvalidTokenException;
import site.bookmore.bookmore.s3.AwsS3Uploader;
import site.bookmore.bookmore.security.provider.JwtProvider;
import site.bookmore.bookmore.users.dto.UserJoinRequest;
import site.bookmore.bookmore.users.dto.UserLoginRequest;
import site.bookmore.bookmore.users.dto.UserUpdateRequest;
import site.bookmore.bookmore.users.entity.Ranks;
import site.bookmore.bookmore.users.entity.User;
import site.bookmore.bookmore.users.repositroy.FollowRepository;
import site.bookmore.bookmore.users.repositroy.RanksRepository;
import site.bookmore.bookmore.users.repositroy.UserRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static site.bookmore.bookmore.common.exception.ErrorCode.*;


class UserServiceTest {

    private final UserRepository userRepository = mock(UserRepository.class);

    private final RanksRepository ranksRepository = mock(RanksRepository.class);
    private final FollowRepository followRepository = mock(FollowRepository.class);

    private final JwtProvider jwtProvider = mock(JwtProvider.class);

    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final AwsS3Uploader awsS3Uploader = mock(AwsS3Uploader.class);
    private final AlarmRepository alarmRepository = mock(AlarmRepository.class);

    private final UserService userService = new UserService(passwordEncoder, jwtProvider, userRepository, ranksRepository, followRepository, awsS3Uploader, alarmRepository);

    private final User user = User.builder()
            .id(0L)
            .email("email")
            .nickname("nickname")
            .password("password")
            .build();


    @Test
    @DisplayName("???????????? - ??????")
    void join_success() {
        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));
        when(userRepository.findByNickname(user.getNickname()))
                .thenReturn(Optional.of(user));

        when(userRepository.save(any(User.class)))
                .thenReturn(user);
        when(ranksRepository.findTop1ByOrderByRankingDesc()).thenReturn(Optional.empty());

        Assertions.assertDoesNotThrow(() -> userService.join(new UserJoinRequest()));

    }


    @Test
    @DisplayName("???????????? - ??????(????????? ??????")
    void join_fail_1() {

        // ????????? ????????? ?????? ??????????????? ????????? ??? ????????? ?????????
        when(userRepository.findByEmail(user.getEmail())) //
                .thenReturn(Optional.of(user)); // ????????? ????????? ??????

        AbstractAppException abstractAppException = assertThrows(DuplicateEmailException.class, () -> {
            userService.join(new UserJoinRequest(user.getEmail(), "password", "nickname", LocalDate.of(2022, 04, 03)));
        });

        assertThat(abstractAppException.getErrorCode()).isEqualTo(DUPLICATED_EMAIL);
    }

    @Test
    @DisplayName("???????????? - ??????(????????? ??????)")
    void join_fail_2() {

        // ????????? ????????? ?????? ??????????????? ????????? ??? ????????? ?????????
        when(userRepository.findByNickname(user.getNickname())) //
                .thenReturn(Optional.of(user)); // ????????? ????????? ??????

        AbstractAppException abstractAppException = assertThrows(DuplicateNicknameException.class, () -> {
            userService.join(new UserJoinRequest(user.getEmail(), "password", "nickname", LocalDate.of(2022, 04, 03)));
        });

        assertThat(abstractAppException.getErrorCode()).isEqualTo(DUPLICATED_NICKNAME);
    }

    @Test
    @DisplayName("????????? - ??????")
    void login_success() {
        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        when(!passwordEncoder.matches(user.getPassword(), "password"))
                .thenReturn(true);

        Assertions.assertDoesNotThrow(() -> userService.login(new UserLoginRequest(user.getEmail(), user.getPassword())));
    }

    @Test
    @DisplayName("????????? - ??????(????????? ?????? ??? ??????)")
    void login_fail_1() {
        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        AbstractAppException abstractAppException = assertThrows(UserNotFoundException.class, () -> {
            userService.login(new UserLoginRequest("test@gmail.com", user.getPassword()));
        });

        assertThat(abstractAppException.getErrorCode()).isEqualTo(USER_NOT_FOUND);
    }

    @Test
    @DisplayName("????????? - ??????(???????????? ??????)")
    void login_fail_2() {
        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        when(!passwordEncoder.matches(user.getPassword(), "password"))
                .thenReturn(true);

        AbstractAppException abstractAppException = assertThrows(InvalidPasswordException.class, () -> {
            userService.login(new UserLoginRequest(user.getEmail(), "failPassword"));
        });

        assertThat(abstractAppException.getErrorCode()).isEqualTo(INVALID_PASSWORD);
    }


    @Test
    @DisplayName("?????? ?????? ?????? - ??????")
    void infoUpdate_success() {
        when(userRepository.findByEmailAndDeletedDatetimeIsNull(user.getEmail()))
                .thenReturn(Optional.of(user));

        Assertions.assertDoesNotThrow(() -> userService.infoUpdate(user.getEmail(), 0L, new UserUpdateRequest("updatePw", null, null)));
    }

    @Test
    @DisplayName("?????? ?????? ?????? - ??????(????????? ??????)")
    void infoUpdate_fail_1() {
        when(userRepository.findByEmailAndDeletedDatetimeIsNull(user.getEmail()))
                .thenReturn(Optional.of(user));

        AbstractAppException abstractAppException = assertThrows(InvalidTokenException.class, () -> {
            userService.infoUpdate(user.getEmail(), 1L, new UserUpdateRequest("updatePw", null, null));
        });

        assertThat(abstractAppException.getErrorCode()).isEqualTo(INVALID_TOKEN);
    }


    @Test
    @DisplayName("?????? ?????? ?????? - ??????(????????? ??????)")
    void infoUpdate_fail_2() {
        User user2 = User.builder()
                .email("test@test.com")
                .nickname("nickname2")
                .build();

        UserUpdateRequest userUpdateRequest = new UserUpdateRequest("updatePw", "nickname2", null);

        when(userRepository.findByEmailAndDeletedDatetimeIsNull(user.getEmail()))
                .thenReturn(Optional.of(user));

        when(userRepository.findByNickname(userUpdateRequest.getNickname()))
                .thenReturn(Optional.of(user2));

        AbstractAppException abstractAppException = assertThrows(DuplicateNicknameException.class, () -> {
            userService.infoUpdate(user.getEmail(), 0L, userUpdateRequest);
        });

        assertThat(abstractAppException.getErrorCode()).isEqualTo(DUPLICATED_NICKNAME);
    }


    @Test
    @DisplayName("?????? ?????? ?????? - ??????")
    void userDelete_success() {

        when(userRepository.findByEmailAndDeletedDatetimeIsNull(user.getEmail()))
                .thenReturn(Optional.of(user));
        when(userRepository.findByIdAndDeletedDatetimeIsNull(0L))
                .thenReturn(Optional.of(user));
        when(ranksRepository.findByUser(any(User.class)))
                .thenReturn(Optional.of(Ranks.of(0, 1L, user)));


        Assertions.assertDoesNotThrow(() -> userService.delete(user.getEmail(), 0L));
    }

    @Test
    @DisplayName("?????? ?????? ?????? - ??????(????????? ??????)")
    void userDelete_fail() {

        when(userRepository.findByEmailAndDeletedDatetimeIsNull(user.getEmail()))
                .thenReturn(Optional.of(user));


        AbstractAppException abstractAppException = assertThrows(InvalidTokenException.class, () -> {
            userService.delete(user.getEmail(), 1L);
        });

        assertThat(abstractAppException.getErrorCode()).isEqualTo(INVALID_TOKEN);
    }

    @Test
    @DisplayName("?????? ?????? ?????? - ??????")
    void verify_success() {
        when(userRepository.findByEmailAndDeletedDatetimeIsNull(user.getEmail()))
                .thenReturn(Optional.of(user));

        Assertions.assertDoesNotThrow(() -> userService.verify(user.getEmail()));
    }

    @Test
    @DisplayName("?????? ?????? ?????? - ??????")
    void verify_fail() {
        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        AbstractAppException abstractAppException = assertThrows(UserNotFoundException.class, () -> {
            userService.verify("test@gmail.com");
        });

        assertThat(abstractAppException.getErrorCode()).isEqualTo(USER_NOT_FOUND);
    }

    @Test
    @DisplayName("?????? ?????? ?????? ??????")
    void detail_success() {
        when(userRepository.findByIdAndDeletedDatetimeIsNull(user.getId()))
                .thenReturn(Optional.of(user));

        Assertions.assertDoesNotThrow(() -> userService.detail(user.getId()));
    }

    @Test
    @DisplayName("?????? ?????? ?????? ??????(1) - ?????? ????????? ?????? ??? ??????")
    void detail_fail() {

        UserNotFoundException exception = Assertions.assertThrows(UserNotFoundException.class, () ->
                userService.detail(user.getId()));

        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("?????? - ??????")
    void search_success() {
        when(userRepository.findByEmailAndDeletedDatetimeIsNull(user.getEmail()))
                .thenReturn(Optional.of(user));

        Assertions.assertDoesNotThrow(() -> userService.search(user.getEmail()));
    }

    @Test
    @DisplayName("?????? - ??????")
    void search_fail() {
        UserNotFoundException exception = Assertions.assertThrows(UserNotFoundException.class, () ->
                userService.search(user.getEmail()));

        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("?????? ?????? ?????? - ??????")
    void infoEdit_success() {
        when(userRepository.findByEmailAndDeletedDatetimeIsNull(user.getEmail()))
                .thenReturn(Optional.of(user));

        Assertions.assertDoesNotThrow(() -> userService.infoEdit(user.getEmail(), new UserUpdateRequest("updatePw", null, null)));
    }



}