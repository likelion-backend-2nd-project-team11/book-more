package site.bookmore.bookmore.users.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import site.bookmore.bookmore.common.exception.ErrorCode;
import site.bookmore.bookmore.common.exception.bad_request.FollowNotMeException;
import site.bookmore.bookmore.common.exception.conflict.DuplicateFollowException;
import site.bookmore.bookmore.common.exception.not_found.FollowNotFoundException;
import site.bookmore.bookmore.common.exception.not_found.UserNotFoundException;
import site.bookmore.bookmore.users.dto.FollowerResponse;
import site.bookmore.bookmore.users.dto.FollowingResponse;
import site.bookmore.bookmore.users.entity.Follow;
import site.bookmore.bookmore.users.entity.User;
import site.bookmore.bookmore.users.repositroy.FollowRepository;
import site.bookmore.bookmore.users.repositroy.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

class FollowServiceTest {

    FollowService followService;
    FollowRepository followRepository = mock(FollowRepository.class);
    UserRepository userRepository = mock(UserRepository.class);
    private final ApplicationEventPublisher publisher = mock(ApplicationEventPublisher.class);

    @BeforeEach
    void setUp() {
        followService = new FollowService(followRepository, userRepository, publisher);
    }

    @Test
    @DisplayName("팔로우 성공")
    void follow_success() {
        User user = User.builder()
                .id(1L)
                .email("123@naver.com")
                .password("123qwer")
                .nickname("AA")
                .build();

        User targetUser = User.builder()
                .id(2L)
                .email("456@naver.com")
                .password("456qwer")
                .nickname("BB")
                .build();

        Follow follow = Follow.builder()
                .id(1L)
                .following(targetUser)
                .follower(user)
                .build();

        Mockito.when(userRepository.findByEmailAndDeletedDatetimeIsNull(user.getEmail()))
                .thenReturn(Optional.of(user));

        Mockito.when(userRepository.findByIdAndDeletedDatetimeIsNull(targetUser.getId()))
                .thenReturn(Optional.of(targetUser));

        Mockito.when(followRepository.findByFollowerAndFollowing(user, targetUser))
                .thenReturn(Optional.of(follow));

        Mockito.when(followRepository.save(follow))
                .thenReturn(follow);

        String result = Assertions.assertDoesNotThrow(() -> followService.following(targetUser.getId(), user.getEmail()));

        assertEquals(String.format("%s 님을 팔로우 하셨습니다.", targetUser.getId()), result);
        assertNull(follow.getDeletedDatetime());
    }

    @Test
    @DisplayName("팔로우 실패(1) - 로그인하지 않은 유저가 팔로우 하려고 하는 경우")
    void follow_fail_1() {
        User user = User.builder()
                .id(1L)
                .email("123@naver.com")
                .password("123qwer")
                .nickname("AA")
                .build();

        User targetUser = User.builder()
                .id(2L)
                .email("456@naver.com")
                .password("456qwer")
                .nickname("BB")
                .build();

        Follow follow = Follow.builder()
                .id(1L)
                .following(targetUser)
                .follower(user)
                .build();

        Mockito.when(userRepository.findById(targetUser.getId()))
                .thenReturn(Optional.of(targetUser));

        Mockito.when(followRepository.findByFollowerAndFollowing(user, targetUser))
                .thenReturn(Optional.of(follow));

        Mockito.when(followRepository.save(follow))
                .thenReturn(follow);

        UserNotFoundException exception = Assertions.assertThrows(UserNotFoundException.class, () -> {
            followService.following(targetUser.getId(), user.getEmail());
        });

        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("팔로우 실패(2) - 없는 유저를 팔로우한 경우")
    void follow_fail_2() {
        User user = User.builder()
                .id(1L)
                .email("123@naver.com")
                .password("123qwer")
                .nickname("AA")
                .build();

        User targetUser = User.builder()
                .id(2L)
                .email("456@naver.com")
                .password("456qwer")
                .nickname("BB")
                .build();

        Follow follow = Follow.builder()
                .id(1L)
                .following(targetUser)
                .follower(user)
                .build();

        Mockito.when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        Mockito.when(followRepository.findByFollowerAndFollowing(user, targetUser))
                .thenReturn(Optional.of(follow));

        Mockito.when(followRepository.save(follow))
                .thenReturn(follow);

        UserNotFoundException exception = Assertions.assertThrows(UserNotFoundException.class, () -> {
            followService.following(targetUser.getId(), user.getEmail());
        });

        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("팔로우 실패(3) - 자기 자신을 팔로우한 경우")
    void follow_fail_3() {
        User user = User.builder()
                .id(1L)
                .email("123@naver.com")
                .password("123qwer")
                .nickname("AA")
                .build();

        Follow follow = Follow.builder()
                .id(1L)
                .following(user)
                .follower(user)
                .build();

        Mockito.when(userRepository.findByEmailAndDeletedDatetimeIsNull(user.getEmail()))
                .thenReturn(Optional.of(user));

        Mockito.when(userRepository.findByIdAndDeletedDatetimeIsNull(user.getId()))
                .thenReturn(Optional.of(user));

        Mockito.when(followRepository.findByFollowerAndFollowing(user, user))
                .thenReturn(Optional.of(follow));

        Mockito.when(followRepository.save(follow))
                .thenReturn(follow);

        FollowNotMeException exception = Assertions.assertThrows(FollowNotMeException.class, () -> {
            followService.following(user.getId(), user.getEmail());
        });

        assertEquals(ErrorCode.FOLLOW_NOT_ME, exception.getErrorCode());
    }

    @Test
    @DisplayName("팔로우 실패(4) -이미 팔로우 한 경우")
    void follow_fail_4() {
        User user = User.builder()
                .id(1L)
                .email("123@naver.com")
                .password("123qwer")
                .nickname("AA")
                .build();

        User targetUser = User.builder()
                .id(2L)
                .email("456@naver.com")
                .password("456qwer")
                .nickname("BB")
                .build();

        Follow follow = Follow.builder()
                .id(1L)
                .following(targetUser)
                .follower(user)
                .build();

        Mockito.when(userRepository.findByEmailAndDeletedDatetimeIsNull(user.getEmail()))
                .thenReturn(Optional.of(user));

        Mockito.when(userRepository.findByIdAndDeletedDatetimeIsNull(targetUser.getId()))
                .thenReturn(Optional.of(targetUser));

        Mockito.when(followRepository.findByFollowerAndFollowingAndDeletedDatetimeIsNull(user, targetUser))
                .thenReturn(Optional.of(follow));

        Mockito.when(followRepository.findByFollowerAndFollowing(user, targetUser))
                .thenReturn(Optional.of(follow));

        Mockito.when(followRepository.save(follow))
                .thenReturn(follow);

        DuplicateFollowException exception = Assertions.assertThrows(DuplicateFollowException.class, () -> {
            followService.following(targetUser.getId(), user.getEmail());
        });

        assertEquals(ErrorCode.DUPLICATED_FOLLOW, exception.getErrorCode());
    }

    @Test
    @DisplayName("언팔로우 성공")
    void unfollow_success() {
        User user = User.builder()
                .id(1L)
                .email("123@naver.com")
                .password("123qwer")
                .nickname("AA")
                .build();

        User targetUser = User.builder()
                .id(2L)
                .email("456@naver.com")
                .password("456qwer")
                .nickname("BB")
                .build();

        Follow follow = Follow.builder()
                .id(1L)
                .following(targetUser)
                .follower(user)
                .build();

        Mockito.when(userRepository.findByEmailAndDeletedDatetimeIsNull(user.getEmail()))
                .thenReturn(Optional.of(user));

        Mockito.when(userRepository.findByIdAndDeletedDatetimeIsNull(targetUser.getId()))
                .thenReturn(Optional.of(targetUser));

        Mockito.when(followRepository.findByFollowerAndFollowing(any(User.class), any(User.class)))
                .thenReturn(Optional.of(follow));

        String result = Assertions.assertDoesNotThrow(() -> followService.unfollowing(targetUser.getId(), user.getEmail()));

        assertEquals(String.format("%s 님을 언팔로우 하셨습니다.", targetUser.getId()), result);
        assertNotNull(follow.getDeletedDatetime());
    }

    @Test
    @DisplayName("언팔로우 실패(1) - 로그인하지 않은 유저가 언팔로우 하려고 하는 경우")
    void unfollow_fail_1() {
        User user = User.builder()
                .id(1L)
                .email("123@naver.com")
                .password("123qwer")
                .nickname("AA")
                .build();

        User targetUser = User.builder()
                .id(2L)
                .email("456@naver.com")
                .password("456qwer")
                .nickname("BB")
                .build();

        Follow follow = Follow.builder()
                .id(1L)
                .following(targetUser)
                .follower(user)
                .build();

        Mockito.when(userRepository.findById(targetUser.getId()))
                .thenReturn(Optional.of(targetUser));

        Mockito.when(followRepository.findByFollowerAndFollowing(any(User.class), any(User.class)))
                .thenReturn(Optional.of(follow));

        UserNotFoundException exception = Assertions.assertThrows(UserNotFoundException.class, () -> {
            followService.unfollowing(targetUser.getId(), user.getEmail());
        });

        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("언팔로우 실패(2) - 없는 유저를 언팔로우 하는 경우")
    void unfollow_fail_2() {
        User user = User.builder()
                .id(1L)
                .email("123@naver.com")
                .password("123qwer")
                .nickname("AA")
                .build();

        User targetUser = User.builder()
                .id(2L)
                .email("456@naver.com")
                .password("456qwer")
                .nickname("BB")
                .build();

        Follow follow = Follow.builder()
                .id(1L)
                .following(targetUser)
                .follower(user)
                .build();

        Mockito.when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        Mockito.when(followRepository.findByFollowerAndFollowing(any(User.class), any(User.class)))
                .thenReturn(Optional.of(follow));

        UserNotFoundException exception = Assertions.assertThrows(UserNotFoundException.class, () -> {
            followService.unfollowing(targetUser.getId(), user.getEmail());
        });

        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("언팔로우 실패(3) - 팔로우하지 않은 사람을 언팔로우 하는 경우")
    void unfollow_fail_3() {
        User user = User.builder()
                .id(1L)
                .email("123@naver.com")
                .password("123qwer")
                .nickname("AA")
                .build();

        User targetUser = User.builder()
                .id(2L)
                .email("456@naver.com")
                .password("456qwer")
                .nickname("BB")
                .build();

        Mockito.when(userRepository.findByEmailAndDeletedDatetimeIsNull(user.getEmail()))
                .thenReturn(Optional.of(user));

        Mockito.when(userRepository.findByIdAndDeletedDatetimeIsNull(targetUser.getId()))
                .thenReturn(Optional.of(targetUser));

        FollowNotFoundException exception = Assertions.assertThrows(FollowNotFoundException.class, () -> {
            followService.unfollowing(targetUser.getId(), user.getEmail());
        });

        assertEquals(ErrorCode.FOLLOW_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("언팔로우 실패(4) - 언팔로우를 이미 한 경우")
    void unfollow_fail_4() {
        User user = User.builder()
                .id(1L)
                .email("123@naver.com")
                .password("123qwer")
                .nickname("AA")
                .build();

        User targetUser = User.builder()
                .id(2L)
                .email("456@naver.com")
                .password("456qwer")
                .nickname("BB")
                .build();

        Follow follow = Follow.builder()
                .id(1L)
                .following(targetUser)
                .follower(user)
                .build();

        follow.delete();

        Mockito.when(userRepository.findByEmailAndDeletedDatetimeIsNull(user.getEmail()))
                .thenReturn(Optional.of(user));

        Mockito.when(userRepository.findByIdAndDeletedDatetimeIsNull(targetUser.getId()))
                .thenReturn(Optional.of(targetUser));

        Mockito.when(followRepository.findByFollowerAndFollowing(any(User.class), any(User.class)))
                .thenReturn(Optional.of(follow));

        FollowNotFoundException exception = Assertions.assertThrows(FollowNotFoundException.class, () -> {
            followService.unfollowing(targetUser.getId(), user.getEmail());
        });

        assertEquals(ErrorCode.FOLLOW_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("모든 팔로잉 조회 성공")
    void findAllFollowing_success() {

        User user = User.builder()
                .id(1L)
                .email("123@naver.com")
                .password("123qwer")
                .nickname("AA")
                .build();

        User targetUser = User.builder()
                .id(2L)
                .email("456@naver.com")
                .password("456qwer")
                .nickname("BB")
                .build();

        Follow follow = Follow.builder()
                .id(1L)
                .following(targetUser)
                .follower(user)
                .build();

        Follow follow2 = Follow.builder()
                .id(2L)
                .following(user)
                .follower(targetUser)
                .build();

        Pageable pageable = PageRequest.of(0, 20);
        List<Follow> followings = Arrays.asList(follow, follow2);
        Page<Follow> followPage = new PageImpl<>(followings);

        Mockito.when(userRepository.findByIdAndDeletedDatetimeIsNull(user.getId()))
                .thenReturn(Optional.of(user));

        Mockito.when(followRepository.findByFollowerAndDeletedDatetimeIsNull(pageable, user)).thenReturn(followPage);

        Page<FollowingResponse> result = Assertions.assertDoesNotThrow(() -> followService.findAllFollowing(user.getId(), pageable));

        assertEquals(2, result.getNumberOfElements());
        assertEquals(targetUser.getNickname(), result.getContent().get(0).getNickname());
        assertEquals(targetUser.getProfile(), result.getContent().get(0).getProfile());
        assertEquals(targetUser.getFollowCount().getFollowerCount(), result.getContent().get(0).getFollowerCount());
        assertEquals(targetUser.getFollowCount().getFollowingCount(), result.getContent().get(0).getFollowingCount());
    }

    @Test
    @DisplayName("모든 팔로잉 조회 실패(1) - 없는 유저의 팔로잉을 조회 한 경우")
    void findAllFollowing_fail_1() {
        User user = User.builder()
                .id(1L)
                .email("123@naver.com")
                .password("123qwer")
                .nickname("AA")
                .build();

        User targetUser = User.builder()
                .id(2L)
                .email("456@naver.com")
                .password("456qwer")
                .nickname("BB")
                .build();

        Follow follow = Follow.builder()
                .id(1L)
                .following(targetUser)
                .follower(user)
                .build();

        Follow follow2 = Follow.builder()
                .id(2L)
                .following(user)
                .follower(targetUser)
                .build();

        Pageable pageable = PageRequest.of(0, 20);
        List<Follow> followings = Arrays.asList(follow, follow2);
        Page<Follow> followPage = new PageImpl<>(followings);

        Mockito.when(userRepository.findByIdAndDeletedDatetimeIsNull(user.getId()))
                .thenReturn(Optional.of(user));

        Mockito.when(followRepository.findByFollowerAndDeletedDatetimeIsNull(pageable, user)).thenReturn(followPage);

        UserNotFoundException exception = Assertions.assertThrows(UserNotFoundException.class, () ->
                followService.findAllFollowing(100L, pageable));

        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());

    }

    @Test
    @DisplayName("모든 팔로워 조회 성공")
    void findAllFollower_success() {

        User user = User.builder()
                .id(1L)
                .email("123@naver.com")
                .password("123qwer")
                .nickname("AA")
                .build();

        User targetUser = User.builder()
                .id(2L)
                .email("456@naver.com")
                .password("456qwer")
                .nickname("BB")
                .build();

        Follow follow = Follow.builder()
                .id(1L)
                .following(targetUser)
                .follower(user)
                .build();

        Follow follow2 = Follow.builder()
                .id(2L)
                .following(user)
                .follower(targetUser)
                .build();

        Pageable pageable = PageRequest.of(0, 20);
        List<Follow> followers = Arrays.asList(follow, follow2);
        Page<Follow> followPage = new PageImpl<>(followers);

        Mockito.when(userRepository.findByIdAndDeletedDatetimeIsNull(user.getId()))
                .thenReturn(Optional.of(user));

        Mockito.when(followRepository.findByFollowingAndDeletedDatetimeIsNull(pageable, user)).thenReturn(followPage);

        Page<FollowerResponse> result = Assertions.assertDoesNotThrow(() -> followService.findAllFollower(user.getId(), pageable));

        assertEquals(2, result.getNumberOfElements());
        assertEquals(user.getNickname(), result.getContent().get(0).getNickname());
        assertEquals(user.getProfile(), result.getContent().get(0).getProfile());
        assertEquals(user.getFollowCount().getFollowerCount(), result.getContent().get(0).getFollowerCount());
        assertEquals(user.getFollowCount().getFollowingCount(), result.getContent().get(0).getFollowingCount());
    }

    @Test
    @DisplayName("모든 팔로워 조회 실패(1) - 없는 유저의 팔로워를 조회 한 경우")
    void findAllFollower_fail_1() {
        User user = User.builder()
                .id(1L)
                .email("123@naver.com")
                .password("123qwer")
                .nickname("AA")
                .build();

        User targetUser = User.builder()
                .id(2L)
                .email("456@naver.com")
                .password("456qwer")
                .nickname("BB")
                .build();

        Follow follow = Follow.builder()
                .id(1L)
                .following(targetUser)
                .follower(user)
                .build();

        Follow follow2 = Follow.builder()
                .id(2L)
                .following(user)
                .follower(targetUser)
                .build();

        Pageable pageable = PageRequest.of(0, 20);
        List<Follow> followers = Arrays.asList(follow, follow2);
        Page<Follow> followPage = new PageImpl<>(followers);

        Mockito.when(userRepository.findByIdAndDeletedDatetimeIsNull(user.getId()))
                .thenReturn(Optional.of(user));

        Mockito.when(followRepository.findByFollowingAndDeletedDatetimeIsNull(pageable, user)).thenReturn(followPage);

        UserNotFoundException exception = Assertions.assertThrows(UserNotFoundException.class, () ->
                followService.findAllFollower(100L, pageable));

        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("팔로우 인지 아닌지 출력 성공(1) - 팔로우 인 경우")
    void isFollow_success_1() {
        User user = User.builder()
                .id(1L)
                .email("123@naver.com")
                .password("123qwer")
                .nickname("AA")
                .build();

        User targetUser = User.builder()
                .id(2L)
                .email("456@naver.com")
                .password("456qwer")
                .nickname("BB")
                .build();

        Follow follow = Follow.builder()
                .id(1L)
                .following(targetUser)
                .follower(user)
                .build();

        Mockito.when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        Mockito.when(userRepository.findById(targetUser.getId()))
                .thenReturn(Optional.of(targetUser));

        Mockito.when(followRepository.findByFollowerAndFollowingAndDeletedDatetimeIsNull(user, targetUser))
                .thenReturn(Optional.of(follow));

        Boolean result = Assertions.assertDoesNotThrow(() -> followService.isFollow(targetUser.getId(), user.getEmail()));

        assertEquals(true, result);
    }

    @Test
    @DisplayName("팔로우 인지 아닌지 출력 성공(2) - 팔로우가 아닌 경우")
    void isFollow_success_2() {
        User user = User.builder()
                .id(1L)
                .email("123@naver.com")
                .password("123qwer")
                .nickname("AA")
                .build();

        User targetUser = User.builder()
                .id(2L)
                .email("456@naver.com")
                .password("456qwer")
                .nickname("BB")
                .build();

        Follow follow = Follow.builder()
                .id(1L)
                .following(targetUser)
                .follower(user)
                .build();

        Mockito.when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        Mockito.when(userRepository.findById(targetUser.getId()))
                .thenReturn(Optional.of(targetUser));

        Boolean result = Assertions.assertDoesNotThrow(() -> followService.isFollow(targetUser.getId(), user.getEmail()));

        assertEquals(false, result);
    }

    @Test
    @DisplayName("팔로우 인지 아닌지 출력 실패(1) - 로그인하지 않은 유저가 팔로우 하려고 하는 경우")
    void isFollow_fail_1() {
        User user = User.builder()
                .id(1L)
                .email("123@naver.com")
                .password("123qwer")
                .nickname("AA")
                .build();

        User targetUser = User.builder()
                .id(2L)
                .email("456@naver.com")
                .password("456qwer")
                .nickname("BB")
                .build();

        Follow follow = Follow.builder()
                .id(1L)
                .following(targetUser)
                .follower(user)
                .build();

        Mockito.when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        Mockito.when(userRepository.findById(targetUser.getId()))
                .thenReturn(Optional.of(targetUser));

        Mockito.when(followRepository.findByFollowerAndFollowingAndDeletedDatetimeIsNull(user, targetUser))
                .thenReturn(Optional.of(follow));

        UserNotFoundException exception = Assertions.assertThrows(UserNotFoundException.class, () -> {
            followService.isFollow(targetUser.getId(), "789@naver.com");
        });

        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("팔로우 인지 아닌지 출력 실패(2) - 없는 유저를 팔로우한 경우")
    void isFollow_fail_2() {
        User user = User.builder()
                .id(1L)
                .email("123@naver.com")
                .password("123qwer")
                .nickname("AA")
                .build();

        User targetUser = User.builder()
                .id(2L)
                .email("456@naver.com")
                .password("456qwer")
                .nickname("BB")
                .build();

        Follow follow = Follow.builder()
                .id(1L)
                .following(targetUser)
                .follower(user)
                .build();

        Mockito.when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        Mockito.when(userRepository.findById(targetUser.getId()))
                .thenReturn(Optional.of(targetUser));

        Mockito.when(followRepository.findByFollowerAndFollowingAndDeletedDatetimeIsNull(user, targetUser))
                .thenReturn(Optional.of(follow));

        UserNotFoundException exception = Assertions.assertThrows(UserNotFoundException.class, () -> {
            followService.isFollow(100L, user.getEmail());
        });

        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }
}