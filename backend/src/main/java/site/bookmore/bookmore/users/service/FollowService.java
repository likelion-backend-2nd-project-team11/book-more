package site.bookmore.bookmore.users.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.bookmore.bookmore.alarms.entity.AlarmType;
import site.bookmore.bookmore.common.exception.bad_request.FollowNotMeException;
import site.bookmore.bookmore.common.exception.conflict.DuplicateFollowException;
import site.bookmore.bookmore.common.exception.conflict.DuplicateUnfollowException;
import site.bookmore.bookmore.common.exception.not_found.FollowNotFoundException;
import site.bookmore.bookmore.common.exception.not_found.UserNotFoundException;
import site.bookmore.bookmore.observer.event.alarm.AlarmCreate;
import site.bookmore.bookmore.users.dto.FollowerResponse;
import site.bookmore.bookmore.users.dto.FollowingResponse;
import site.bookmore.bookmore.users.entity.Follow;
import site.bookmore.bookmore.users.entity.User;
import site.bookmore.bookmore.users.repositroy.FollowRepository;
import site.bookmore.bookmore.users.repositroy.UserRepository;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher publisher;

    public String following(Long id, String email) {

        //나
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        //팔로우할 유저
        User targetUser = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        //자기 자신을 팔로우한 경우
        if (Objects.equals(user.getId(), id)) {
            throw new FollowNotMeException();
        }

        //이미 팔로우 한 경우(이전에 언팔도 한 적 없음)
        followRepository.findByFollowerAndFollowingAndDeletedDatetimeIsNull(user, targetUser)
                .ifPresent(follow -> {
                    throw new DuplicateFollowException();
                });

        //이전에 언팔한 경우에 다시 팔로우 하는 경우
        Follow follow = followRepository.findByFollowerAndFollowing(user, targetUser)
                .orElse(Follow.builder()
                        .following(targetUser)
                        .follower(user)
                        .build());

        follow.undelete();
        followRepository.save(follow);

        publisher.publishEvent(AlarmCreate.of(AlarmType.NEW_FOLLOW, follow.getFollowing(), user, follow.getId()));

        return String.format("%s 님을 팔로우 하셨습니다.", id);
    }

    @Transactional
    public String unfollowing(Long id, String email) {
        //나
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        //팔로우할 유저
        User targetUser = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        //팔로우 하지 않은 사람을 언팔로우 하는 경우
        Follow targetFollow = followRepository.findByFollowerAndFollowing(user, targetUser)
                .orElseThrow(FollowNotFoundException::new);

        //언팔로우를 이미 한 경우
        if (targetFollow.getDeletedDatetime() != null) {
            throw new DuplicateUnfollowException();
        }

        targetFollow.delete();

        return String.format("%s 님을 언팔로우 하셨습니다.", id);
    }

    public Page<FollowingResponse> findAllFollowing(Long id, Pageable pageable) {

        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        return followRepository.findByFollowerAndDeletedDatetimeIsNull(pageable, user).map(FollowingResponse::new);
    }

    public Page<FollowerResponse> findAllFollower(Long id, Pageable pageable) {

        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        return followRepository.findByFollowingAndDeletedDatetimeIsNull(pageable, user).map(FollowerResponse::new);
    }
}
