package site.bookmore.bookmore.users.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.bookmore.bookmore.common.exception.conflict.DuplicateEmailException;
import site.bookmore.bookmore.common.exception.conflict.DuplicateNicknameException;
import site.bookmore.bookmore.common.exception.not_found.UserNotFoundException;
import site.bookmore.bookmore.common.exception.unauthorized.InvalidPasswordException;
import site.bookmore.bookmore.common.exception.unauthorized.InvalidTokenException;
import site.bookmore.bookmore.security.provider.JwtProvider;
import site.bookmore.bookmore.users.dto.*;
import site.bookmore.bookmore.users.entity.User;
import site.bookmore.bookmore.users.repositroy.UserRepository;


@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
    }

    /**
     * 회원 가입
     */
    public UserJoinResponse join(UserJoinRequest userJoinRequest) {
        userRepository.findByEmail(userJoinRequest.getEmail()).ifPresent(user -> {
            throw new DuplicateEmailException();
        });
        userRepository.findByNickname(userJoinRequest.getNickname()).ifPresent(user -> {
            throw new DuplicateNicknameException();
        });
        String encoded = passwordEncoder.encode(userJoinRequest.getPassword());
        User user = userRepository.save(userJoinRequest.toEntity(encoded));

        return UserJoinResponse.of(user);
    }

    /**
     * 로그인
     */
    public UserLoginResponse login(UserLoginRequest userLoginRequest) {
        User user = userRepository.findByEmail(userLoginRequest.getEmail()).orElseThrow(UserNotFoundException::new);
        if (!passwordEncoder.matches(userLoginRequest.getPassword(), user.getPassword()))
            throw new InvalidPasswordException();
        return new UserLoginResponse(jwtProvider.generateToken(user));
    }


    /**
     * 회원 정보 수정
     */

    @Transactional
    public UserUpdateResponse infoUpdate(String email, Long userId, UserUpdateRequest userUpdateRequest) {

        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

        // id, 토큰 아이디 확인
        if (!user.getId().equals(userId)) throw new InvalidTokenException();

        // 중복 이름 예외처리
        if (userUpdateRequest.getNickname() != null) {
            userRepository.findByNickname(userUpdateRequest.getNickname())
                    .ifPresent(user1 -> {
                        throw new DuplicateNicknameException();
                    });
        }

        // password encode
        String encoded = userUpdateRequest.getPassword();
        if (encoded == null) {
            encoded = user.getPassword();
        }
        String encodedPw = passwordEncoder.encode(encoded);

        user.update(userUpdateRequest.toEntity(encodedPw));

        return UserUpdateResponse.of(user);
    }

}
