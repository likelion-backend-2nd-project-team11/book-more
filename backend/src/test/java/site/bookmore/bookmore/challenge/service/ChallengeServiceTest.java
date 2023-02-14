package site.bookmore.bookmore.challenge.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import site.bookmore.bookmore.challenge.dto.ChallengeDetailResponse;
import site.bookmore.bookmore.challenge.dto.ChallengeRequest;
import site.bookmore.bookmore.challenge.entity.Challenge;
import site.bookmore.bookmore.challenge.repository.ChallengeRepository;
import site.bookmore.bookmore.common.exception.ErrorCode;
import site.bookmore.bookmore.common.exception.not_found.UserNotFoundException;
import site.bookmore.bookmore.users.entity.User;
import site.bookmore.bookmore.users.repositroy.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ChallengeServiceTest {

    ChallengeService challengeService;

    ChallengeRepository challengeRepository = mock(ChallengeRepository.class);
    UserRepository userRepository = mock(UserRepository.class);

    @InjectMocks
    Pageable pageable = PageRequest.of(0, 20);
    private final ChallengeRequest challengeRequest = ChallengeRequest.builder()
            .title("title")
            .description("description")
            .deadline(LocalDate.of(2023, 01, 20))
            .build();

    private final User mockUser = User.builder()
            .id(1L)
            .email("test@tes.com")
            .nickname("nickname")
            .build();

    private final Challenge challenge = Challenge.builder()
            .id(1L)
            .title("title")
            .description("description")
            .owner(mockUser)
            .deadline(LocalDate.of(2023, 01, 20))
            .build();

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
    @DisplayName("challenge 수정 성공")
    void challenge_modifySuccess() {

        when(userRepository.findByEmailAndDeletedDatetimeIsNull(mockUser.getEmail()))
                .thenReturn(of(mockUser));

        when(challengeRepository.findById(challenge.getId()))
                .thenReturn(of(challenge));

        challenge.update(challengeRequest.toEntity());

        Assertions.assertDoesNotThrow(() -> challengeService.modify(mockUser.getEmail(), challenge.getId(), challengeRequest));
    }

    @Test
    @DisplayName("challenge 수정 실패")
    void modify_fail() {
        User mockUser2 = User.builder()
                .id(2L)
                .email("test2@tes.com")
                .nickname("nickname2")
                .build();

        Challenge challenge2 = Challenge.builder()
                .id(2L)
                .title("title2")
                .description("description2")
                .owner(mockUser2)
                .deadline(LocalDate.of(2023, 01, 20))
                .build();

        when(userRepository.findByEmailAndDeletedDatetimeIsNull(mockUser.getEmail()))
                .thenReturn(of(mockUser));
        when(challengeRepository.findById(challenge.getId()))
                .thenReturn(of(challenge));
        when(challengeRepository.findById(challenge2.getId()))
                .thenReturn(of(challenge2));

        UserNotFoundException exception = Assertions.assertThrows(UserNotFoundException.class, () -> {
            challengeService.modify(mockUser.getEmail(), challenge2.getId(), challengeRequest);
        });

        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("challenge 삭제 성공")
    void challenge_deleteSuccess() {
        when(userRepository.findByEmailAndDeletedDatetimeIsNull(mockUser.getEmail()))
                .thenReturn(of(mockUser));
        when(challengeRepository.findById(challenge.getId()))
                .thenReturn(of(challenge));

        Assertions.assertDoesNotThrow(() -> challengeService.delete(mockUser.getEmail(), challenge.getId()));
    }

    @Test
    @DisplayName("challenge 삭제 실패")
    void delete_fail() {
        User mockUser2 = User.builder()
                .id(2L)
                .email("test2@tes.com")
                .nickname("nickname2")
                .build();

        Challenge challenge2 = Challenge.builder()
                .id(2L)
                .title("title2")
                .description("description2")
                .owner(mockUser2)
                .deadline(LocalDate.of(2023, 01, 20))
                .build();

        when(userRepository.findByEmailAndDeletedDatetimeIsNull(mockUser.getEmail()))
                .thenReturn(of(mockUser));

        when(challengeRepository.findById(challenge.getId()))
                .thenReturn(of(challenge));
        when(challengeRepository.findById(challenge2.getId()))
                .thenReturn(of(challenge2));

        UserNotFoundException exception = Assertions.assertThrows(UserNotFoundException.class, () -> {
            challengeService.delete(mockUser.getEmail(), challenge2.getId());
        });

        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("챌린지 조회")
    void getChallenge() {
        Challenge challenge2 = Challenge.builder()
                .id(1L)
                .title("title")
                .description("description")
                .owner(mockUser)
                .progress(0)
                .deadline(LocalDate.of(2023, 01, 20))
                .build();

        when(userRepository.findByEmailAndDeletedDatetimeIsNull(mockUser.getEmail()))
                .thenReturn(of(mockUser));
        when(challengeRepository.findById(challenge2.getId()))
                .thenReturn(of(challenge2));

        ChallengeDetailResponse cdr = challengeService.get(mockUser.getEmail(), challenge2.getId());

        assertThat(cdr.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("챌린지 list 조회")
    void getChallenge_list() {
        Challenge challenge2 = Challenge.builder()
                .id(1L)
                .title("title")
                .description("description")
                .owner(mockUser)
                .progress(0)
                .deadline(LocalDate.of(2023, 01, 20))
                .build();

        Challenge challenge3 = Challenge.builder()
                .id(2L)
                .title("title2")
                .description("description2")
                .owner(mockUser)
                .progress(0)
                .deadline(LocalDate.of(2023, 01, 20))
                .build();

        Page<Challenge> challenges = new PageImpl<>(List.of(challenge2, challenge3));

        when(userRepository.findByEmailAndDeletedDatetimeIsNull(mockUser.getEmail()))
                .thenReturn(of(mockUser));
        when(challengeRepository.findByOwner(pageable, mockUser))
                .thenReturn(challenges);

        Page<ChallengeDetailResponse> responses = challengeService.list(pageable, mockUser.getEmail());

        assertThat(responses.getTotalElements()).isEqualTo(2);

    }
}