package site.bookmore.bookmore.users.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import site.bookmore.bookmore.common.exception.conflict.DuplicateEmailException;
import site.bookmore.bookmore.common.exception.conflict.DuplicateNicknameException;
import site.bookmore.bookmore.common.exception.conflict.DuplicateProfileException;
import site.bookmore.bookmore.common.exception.not_found.AlreadyDeletedUserException;
import site.bookmore.bookmore.common.exception.not_found.RanksNotFoundException;
import site.bookmore.bookmore.common.exception.not_found.UserNotFoundException;
import site.bookmore.bookmore.common.exception.unauthorized.InvalidPasswordException;
import site.bookmore.bookmore.common.exception.unauthorized.InvalidTokenException;
import site.bookmore.bookmore.s3.AwsS3Uploader;
import site.bookmore.bookmore.security.provider.JwtProvider;
import site.bookmore.bookmore.users.dto.*;
import site.bookmore.bookmore.users.entity.Follow;
import site.bookmore.bookmore.users.entity.Ranks;
import site.bookmore.bookmore.users.entity.Role;
import site.bookmore.bookmore.users.entity.User;
import site.bookmore.bookmore.users.repositroy.FollowRepository;
import site.bookmore.bookmore.users.repositroy.RanksRepository;
import site.bookmore.bookmore.users.repositroy.UserRepository;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static site.bookmore.bookmore.users.entity.User.DEFAULT_PROFILE_IMG_PATH;


@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final RanksRepository ranksRepository;
    private final FollowRepository followRepository;
    private final AwsS3Uploader awsS3Uploader;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmailAndDeletedDatetimeIsNull(email).orElseThrow(UserNotFoundException::new);
    }

    /**
     * 회원 가입
     */
    @Transactional
    public UserJoinResponse join(UserJoinRequest userJoinRequest) {
        userRepository.findByEmail(userJoinRequest.getEmail()).ifPresent(user -> {
            throw new DuplicateEmailException();
        });
        userRepository.findByNickname(userJoinRequest.getNickname()).ifPresent(user -> {
            throw new DuplicateNicknameException();
        });
        String encoded = passwordEncoder.encode(userJoinRequest.getPassword());


        // 회원 가입시 랭크 등록
        Ranks lastRank = ranksRepository.findTop1ByOrderByRankingDesc().orElse(Ranks.of(0, 1L, null));

        Integer findPoint = lastRank.getPoint();
        Long findRanking = lastRank.getRanking();
        Long ranking = findPoint == 0 ? findRanking : findRanking + 1;

        User user = userJoinRequest.toEntity(encoded);

        userRepository.save(user);

        ranksRepository.save(Ranks.of(0, ranking, user));

        return UserJoinResponse.of(user);
    }

    /**
     * 로그인
     */
    public UserLoginResponse login(UserLoginRequest userLoginRequest) {
        User user = userRepository.findByEmailAndDeletedDatetimeIsNull(userLoginRequest.getEmail()).orElseThrow(UserNotFoundException::new);
        if (!user.isEnabled() && user.getDeletedDatetime() != null) throw new AlreadyDeletedUserException();
        if (!passwordEncoder.matches(userLoginRequest.getPassword(), user.getPassword()))
            throw new InvalidPasswordException();
        return new UserLoginResponse(jwtProvider.generateToken(user));
    }


    /**
     * 회원 정보 수정
     */

    @Transactional
    public UserResponse infoUpdate(String email, Long userId, UserUpdateRequest userUpdateRequest) {

        User user = userRepository.findByEmailAndDeletedDatetimeIsNull(email).orElseThrow(UserNotFoundException::new);

        // id, 토큰 아이디 확인
        if (!user.getId().equals(userId) && user.getRole() != Role.ROLE_ADMIN) throw new InvalidTokenException();

        // 중복 이름 예외처리
        if (userUpdateRequest.getNickname() != null && !userUpdateRequest.getNickname().equals(user.getNickname())) {
            userRepository.findByNickname(userUpdateRequest.getNickname())
                    .ifPresent(user1 -> {
                        throw new DuplicateNicknameException();
                    });
        }

        // password encode
        String password = userUpdateRequest.getPassword();

        User updated = password == null ? userUpdateRequest.toEntity() : userUpdateRequest.toEntity(passwordEncoder.encode(password));

        user.update(updated);

        return UserResponse.of(user, "수정 완료 했습니다.");
    }

    /*내 정보 수정*/
    @Transactional
    public UserPersonalResponse infoEdit(String email, UserUpdateRequest userUpdateRequest) {

        User user = userRepository.findByEmailAndDeletedDatetimeIsNull(email).orElseThrow(UserNotFoundException::new);

        infoUpdate(email, user.getId(), userUpdateRequest);

        return UserPersonalResponse.of(user);
    }


    public UserPersonalResponse search(String email) {
        User user = userRepository.findByEmailAndDeletedDatetimeIsNull(email).orElseThrow(UserNotFoundException::new);
        return UserPersonalResponse.of(user);
    }


    /**
     * 회원 탈퇴
     */
    @Transactional
    public UserResponse delete(String email, Long userId) {
        User user = userRepository.findByEmailAndDeletedDatetimeIsNull(email).orElseThrow(UserNotFoundException::new);

        if (!user.getId().equals(userId) && user.getRole() != Role.ROLE_ADMIN) throw new InvalidTokenException();

        // 팔로우 정리
        List<Follow> follows = followRepository.findAllFollowOfUser(user.getId());
        for (Follow follow : follows) {
            User follower = follow.getFollower();
            User following = follow.getFollowing();
            if (follower.equals(user)) following.getFollowCount().minusFollowerCount(); // 삭제되는 유저가 팔로워인 경우
            else follower.getFollowCount().minusFollowingCount(); // 삭제되는 유저가 팔로잉인 경우
            follow.delete();
        }

        // 랭킹 삭제 처리
        Ranks rank = ranksRepository.findByUser(user).orElseThrow(RanksNotFoundException::new);
        rank.delete();

        user.deactivate();
        user.delete();

        return UserResponse.of(user, "회원 탈퇴 완료.");
    }

    @Transactional
    public UserResponse delete(String email) {
        User user = userRepository.findByEmailAndDeletedDatetimeIsNull(email).orElseThrow(UserNotFoundException::new);
        return delete(email, user.getId());
    }

    public UserJoinResponse verify(String email) {
        User user = userRepository.findByEmailAndDeletedDatetimeIsNull(email).orElseThrow(UserNotFoundException::new);
        return UserJoinResponse.of(user);
    }

    public UserDetailResponse detail(Long id) {
        User user = userRepository.findByIdAndDeletedDatetimeIsNull(id)
                .orElseThrow(UserNotFoundException::new);
        return UserDetailResponse.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .profile(user.getProfile())
                .followingCount(user.getFollowCount().getFollowingCount())
                .followerCount(user.getFollowCount().getFollowerCount())
                .build();
    }

    @Transactional
    public String updateProfile(MultipartFile multipartFile, String email) throws IOException {

        User user = userRepository.findByEmailAndDeletedDatetimeIsNull(email)
                .orElseThrow(UserNotFoundException::new);

        String key = awsS3Uploader.upload(multipartFile);

        user.updateProfile(key);
        return user.getNickname();
    }

    @Transactional
    public String updateProfileDefault(String email) {

        User user = userRepository.findByEmailAndDeletedDatetimeIsNull(email)
                .orElseThrow(UserNotFoundException::new);

        if (Objects.equals(user.getProfile(), DEFAULT_PROFILE_IMG_PATH)) {
            throw new DuplicateProfileException();
        }

        awsS3Uploader.delete(user.getProfile());

        user.updateProfileDefault();
        return user.getNickname();
    }
}