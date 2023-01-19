package site.bookmore.bookmore.users.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import site.bookmore.bookmore.common.exception.bad_request.FollowNotMeException;
import site.bookmore.bookmore.common.exception.not_found.FollowNotFoundException;
import site.bookmore.bookmore.common.exception.not_found.UserNotFoundException;
import site.bookmore.bookmore.users.dto.FollowerResponse;
import site.bookmore.bookmore.users.dto.FollowingResponse;
import site.bookmore.bookmore.users.entity.Follow;
import site.bookmore.bookmore.users.entity.User;
import site.bookmore.bookmore.users.repositroy.FollowRepository;
import site.bookmore.bookmore.users.repositroy.UserRepository;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;

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

        Follow follow = Follow.builder()
                .following(targetUser)
                .follower(user)
                .build();

        follow.undelete();

        followRepository.save(follow);

        return String.format("%s 님을 팔로우 하셨습니다.", id);
    }

    public String unfollowing(Long id, String email) {
        //나
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        //팔로우할 유저
        User targetUser = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        //팔로우 하지 않은 사람을 언팔로우 하는 경우
        Follow targetFollow = followRepository.findByFollowerAndFollowing(user.getId(), targetUser.getId())
                .orElseThrow(FollowNotFoundException::new);

        Follow follow = Follow.builder()
                .following(targetFollow.getFollowing())
                .follower(targetFollow.getFollower())
                .build();

        follow.delete();

        followRepository.save(follow);

        return String.format("%s 님을 언팔로우 하셨습니다.", id);
    }

    public Page<FollowingResponse> findAllFollowing(Long id, Pageable pageable) {

        userRepository.findById(id).orElseThrow(UserNotFoundException::new);

        return followRepository.findAll(pageable).map(follow -> new FollowingResponse(follow));

    }

    public Page<FollowerResponse> findAllFollower(Long id, Pageable pageable) {

        userRepository.findById(id).orElseThrow(UserNotFoundException::new);

        Page<Follow> follows = followRepository.findAll(pageable);

        return followRepository.findAll(pageable).map(follow -> new FollowerResponse(follow));
    }
}
