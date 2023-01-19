package site.bookmore.bookmore.challenge.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import site.bookmore.bookmore.challenge.dto.ChallengeRequest;
import site.bookmore.bookmore.challenge.entity.Challenge;
import site.bookmore.bookmore.challenge.repository.ChallengeRepository;
import site.bookmore.bookmore.common.exception.ErrorCode;
import site.bookmore.bookmore.common.exception.not_found.ReviewNotFoundException;
import site.bookmore.bookmore.common.exception.not_found.UserNotFoundException;
import site.bookmore.bookmore.users.entity.User;
import site.bookmore.bookmore.users.repositroy.UserRepository;

import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ChallengeServiceTest {

    ChallengeService challengeService;

    ChallengeRepository challengeRepository = mock(ChallengeRepository.class);
    UserRepository userRepository = mock(UserRepository.class);

    @BeforeEach
    void setUp(){
        challengeService = new ChallengeService(challengeRepository,userRepository);
    }


    @Test
    @DisplayName("challenge 등록 성공")
    void challenge_addSuccess() {

        Challenge mockChallenge = mock(Challenge.class);
        User mockUser = mock(User.class);

        when(userRepository.findByNickname(any()))
                .thenReturn(of(mockUser));

        when(challengeRepository.save(any()))
                .thenReturn(mockChallenge);

        Assertions.assertDoesNotThrow(() -> challengeService.add("userName","title","description"));
    }

    @Test
    @DisplayName("challenge 등록 실패 (존재하는 회원이 없는 경우)")
    void challengeWriteError() {

        //db에서 회원이 없다면 UserNotFoundException
        when(userRepository.findByNickname(any()))
                .thenThrow(new UserNotFoundException());

        UserNotFoundException userNotFoundException = Assertions.assertThrows(UserNotFoundException.class, () -> challengeService.add(any(), "title","description"));

        assertThat(userNotFoundException.getErrorCode().getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(userNotFoundException.getErrorCode().getMessage());
    }

    @Test
    @DisplayName("challenge 수정 에러 (포스트가 존재하지 않음)")
    void challengeModifyError1() {

        User mockUser = mock(User.class);

        when(userRepository.findByNickname(any()))
                .thenReturn(of(mockUser));

        when(challengeRepository.findById(any()))
                .thenThrow(new ReviewNotFoundException());

        ReviewNotFoundException reviewNotFoundException = Assertions.assertThrows(ReviewNotFoundException.class, () -> challengeService.modify(any(), 1L, new ChallengeRequest("title","description")));

        assertThat(reviewNotFoundException.getErrorCode().getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(reviewNotFoundException.getErrorCode().getMessage());
    }

    @Test
    @DisplayName("challenge 수정 에러 (유저가 존재하지 않음)")
    void challengeModifyError2() {

        when(userRepository.findByNickname("userName"))
                .thenThrow(new UserNotFoundException());

        UserNotFoundException userNotFoundException = Assertions.assertThrows(UserNotFoundException.class, () -> challengeService.modify(any(), 1L, new ChallengeRequest("title","description")));

        assertThat(userNotFoundException.getErrorCode().getHttpStatus());
        assertThat(userNotFoundException.getErrorCode().getMessage());
    }

    @Test
    @DisplayName("challenge 삭제 에러 (포스트가 존재하지 않음)")
    void challengeDeleteError1() {

        User mockUser = mock(User.class);

        when(userRepository.findByNickname(any()))
                .thenReturn(of(mockUser));

        when(challengeRepository.findById(any()))
                .thenThrow(new ReviewNotFoundException());

        ReviewNotFoundException reviewNotFoundException = Assertions.assertThrows(ReviewNotFoundException.class, () -> challengeService.delete("userName",1L));

        assertThat(reviewNotFoundException.getErrorCode().getHttpStatus());
        assertThat(reviewNotFoundException.getErrorCode().getMessage());
    }

    @Test
    @DisplayName("challenge 삭제 에러 (유저가 존재하지 않음)")
    void challengeDeleteError2() {

        when(userRepository.findByNickname("userName"))
                .thenThrow(new UserNotFoundException());

        UserNotFoundException userNotFoundException = Assertions.assertThrows(UserNotFoundException.class, () -> challengeService.delete("userName",1L));

        assertThat(userNotFoundException.getErrorCode().getHttpStatus());
        assertThat(userNotFoundException.getErrorCode().getMessage());
    }
}