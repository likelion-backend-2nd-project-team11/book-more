package site.bookmore.bookmore.challenge.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import site.bookmore.bookmore.challenge.dto.ChallengeRequest;
import site.bookmore.bookmore.challenge.entity.Challenge;
import site.bookmore.bookmore.challenge.repository.ChallengeRepository;
import site.bookmore.bookmore.common.exception.not_found.ReviewNotFoundException;
import site.bookmore.bookmore.common.exception.not_found.UserNotFoundException;
import site.bookmore.bookmore.users.entity.User;
import site.bookmore.bookmore.users.repositroy.UserRepository;

import java.time.LocalDate;
import java.util.Optional;

import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ChallengeServiceTest {

    ChallengeService challengeService;

    ChallengeRepository challengeRepository = mock(ChallengeRepository.class);
    UserRepository userRepository = mock(UserRepository.class);

    @BeforeEach
    void setUp() {
        challengeService = new ChallengeService(challengeRepository, userRepository);
    }


    @Test
    @DisplayName("challenge 등록 성공")
    void challenge_addSuccess() {
        ChallengeRequest challengeRequest = ChallengeRequest.builder()
                .title("title")
                .description("description")
                .deadline(LocalDate.of(2023, 01, 20))
                .build();
        Challenge mockChallenge = mock(Challenge.class);
        User mockUser = mock(User.class);

        when(userRepository.findByEmailAndDeletedDatetimeIsNull(anyString()))
                .thenReturn(of(mockUser));

        when(challengeRepository.save(any()))
                .thenReturn(mockChallenge);

        Assertions.assertDoesNotThrow(() -> challengeService.add("userName", challengeRequest));
    }

    @Test
    @DisplayName("challenge 등록 실패 (존재하는 회원이 없는 경우)")
    void challengeWriteError() {
        ChallengeRequest challengeRequest = ChallengeRequest.builder()
                .title("title")
                .description("description")
                .deadline(LocalDate.of(2023, 01, 20))
                .build();
        //db에서 회원이 없다면 UserNotFoundException
        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());

        UserNotFoundException userNotFoundException = Assertions.assertThrows(UserNotFoundException.class, () -> challengeService.add("test@test.com", challengeRequest));

        assertThat(userNotFoundException.getErrorCode().getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(userNotFoundException.getErrorCode().getMessage());
    }

    @Test
    @DisplayName("challenge 수정 에러 (포스트가 존재하지 않음)")
    void challengeModifyError1() {

        User mockUser = User.builder()
                .id(1L)
                .email("test@tes.com")
                .nickname("nickname")
                .build();

        when(userRepository.findByEmailAndDeletedDatetimeIsNull(anyString()))
                .thenReturn(Optional.of(mockUser));

        when(challengeRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        ReviewNotFoundException reviewNotFoundException = Assertions.assertThrows(ReviewNotFoundException.class, () -> challengeService.modify("test@test.com", 1L, ChallengeRequest.builder().build()));

        assertThat(reviewNotFoundException.getErrorCode().getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(reviewNotFoundException.getErrorCode().getMessage());
    }

    @Test
    @DisplayName("challenge 수정 에러 (유저가 존재하지 않음)")
    void challengeModifyError2() {

        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());

        UserNotFoundException userNotFoundException = Assertions.assertThrows(UserNotFoundException.class, () -> challengeService.modify("test@test.com", 1L, ChallengeRequest.builder().build()));

        assertThat(userNotFoundException.getErrorCode().getHttpStatus());
        assertThat(userNotFoundException.getErrorCode().getMessage());
    }

    @Test
    @DisplayName("challenge 삭제 에러 (챌린지가 존재하지 않음)")
    void challengeDeleteError1() {

        User mockUser = User.builder()
                .id(1L)
                .email("test@tes.com")
                .nickname("nickname")
                .build();

        when(userRepository.findByEmailAndDeletedDatetimeIsNull(anyString()))
                .thenReturn(Optional.of(mockUser));

        when(challengeRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        ReviewNotFoundException reviewNotFoundException = Assertions.assertThrows(ReviewNotFoundException.class, () -> challengeService.delete("userName", 1L));

        assertThat(reviewNotFoundException.getErrorCode().getHttpStatus());
        assertThat(reviewNotFoundException.getErrorCode().getMessage());
    }

    @Test
    @DisplayName("challenge 삭제 에러 (유저가 존재하지 않음)")
    void challengeDeleteError2() {

        when(userRepository.findByNickname("userName"))
                .thenThrow(new UserNotFoundException());

        UserNotFoundException userNotFoundException = Assertions.assertThrows(UserNotFoundException.class, () -> challengeService.delete("userName", 1L));

        assertThat(userNotFoundException.getErrorCode().getHttpStatus());
        assertThat(userNotFoundException.getErrorCode().getMessage());
    }
}