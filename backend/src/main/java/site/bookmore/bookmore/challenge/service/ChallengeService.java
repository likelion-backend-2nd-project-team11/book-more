package site.bookmore.bookmore.challenge.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.bookmore.bookmore.books.dto.ReviewPageResponse;
import site.bookmore.bookmore.challenge.dto.ChallengeDetailResponse;
import site.bookmore.bookmore.challenge.dto.ChallengeRequest;
import site.bookmore.bookmore.challenge.dto.ChallengeResponse;
import site.bookmore.bookmore.challenge.entity.Challenge;
import site.bookmore.bookmore.challenge.repository.ChallengeRepository;
import site.bookmore.bookmore.common.exception.not_found.ReviewNotFoundException;
import site.bookmore.bookmore.common.exception.not_found.UserNotFoundException;
import site.bookmore.bookmore.users.entity.Role;
import site.bookmore.bookmore.users.entity.User;
import site.bookmore.bookmore.users.repositroy.UserRepository;

import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final UserRepository userRepository;

    public ChallengeResponse add(String email, ChallengeRequest challengeRequest) {
        //  토큰으로 로그인한 아이디 비교
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
        Challenge savedChallenge = challengeRequest.toEntity(user);
        challengeRepository.save(savedChallenge);

        return ChallengeResponse.of(savedChallenge,"challenge 등록");
    }

    @Transactional
    public ChallengeResponse modify(String email, Long challengeId, ChallengeRequest challengeRequest) {
        // #1 토큰으로 로그인한 아이디 없을 경우
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
        // #2 수정할 포스트가 없을 경우
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(ReviewNotFoundException::new);
        // #3 사용자와 수정할 포스트의 작성자가 다를 경우 + 계정이 ADMIN 이 아닐 경우
        if (!Objects.equals(challenge.getOwner().getNickname(), user.getNickname()) && !user.getRole().equals(Role.ROLE_ADMIN)) {
            throw new UserNotFoundException();
        }
        //JPA 의 영속성 컨텍스트 덕분에 entity 객체의 값만 변경하면 자동으로 변경사항 반영함!
        //따라서 repository.update 를 쓰지 않아도 됨.
        challenge.update(challengeRequest.toEntity());
        return ChallengeResponse.of(challenge,"challenge 수정 완료");
    }

    public ChallengeResponse delete(String email, Long challengeId) {
        // #1 토큰으로 로그인한 아이디가 없을 경우
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
        // #2 삭제할 포스트가 없을 경우
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(ReviewNotFoundException::new);
        // #3 사용자와 삭제할 포스트의 작성자가 다를 경우
        if (!Objects.equals(challenge.getOwner().getNickname(), user.getNickname()) && !user.getRole().equals(Role.ROLE_ADMIN)) {
            throw new UserNotFoundException();
        }

        challengeRepository.delete(challenge);
        return ChallengeResponse.of(challenge,"challenge 삭제 완료");
    }

    public ChallengeDetailResponse get(String email, Long challengeId) {
        // #1 토큰으로 로그인한 아이디가 없을 경우
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
        // #2 해당 게시글이 존재하지 않을 경우
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(ReviewNotFoundException::new);


        return ChallengeDetailResponse.of(challenge);
    }


    public Page<ChallengeDetailResponse> list(Pageable pageable, String email) {
        Page<Challenge> page = challengeRepository.findByOwner_Email(pageable,email);
//        return page.map(ChallengeDetailResponse::of);
        return challengeRepository.findByOwner_Email(pageable,email).map(ChallengeDetailResponse::of);
    }
}
