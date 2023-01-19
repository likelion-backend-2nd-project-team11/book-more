package site.bookmore.bookmore.challenge.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    public ChallengeResponse add(String email, String title, String description) {
        //  토큰으로 로그인한 아이디 비교
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
        Challenge savedChallenge = Challenge.builder()
                .title(title)
                .description(description)
                .owner(user)
                .build();
        challengeRepository.save(savedChallenge);
        ChallengeResponse challengeResponse = ChallengeResponse.builder()
                .postId(savedChallenge.getId())
                .message("게시글 작성완료")
                .build();
        return challengeResponse;
    }

    @Transactional
    public ChallengeResponse modify(String userName, Long postId, ChallengeRequest challengeRequest) {
        // #1 토큰으로 로그인한 아이디 없을 경우
        User user = userRepository.findByNickname(userName)
                .orElseThrow(UserNotFoundException::new);
        // #2 수정할 포스트가 없을 경우
        Challenge challenge = challengeRepository.findById(postId).orElseThrow(ReviewNotFoundException::new);
        // #3 사용자와 수정할 포스트의 작성자가 다를 경우 + 계정이 ADMIN 이 아닐 경우
        if (!Objects.equals(challenge.getOwner().getNickname(), user.getNickname()) && !user.getRole().equals(Role.ROLE_ADMIN)) {
            throw new UserNotFoundException();
        }
        //JPA 의 영속성 컨텍스트 덕분에 entity 객체의 값만 변경하면 자동으로 변경사항 반영함!
        //따라서 repository.update 를 쓰지 않아도 됨.
        challenge.update(challengeRequest.toEntity());
        ChallengeResponse challengeResponse = ChallengeResponse.builder().postId(postId).message("게시글 수정완료").build();
        return challengeResponse;
    }

    public ChallengeResponse delete(String userName, Long postId) {
        // #1 토큰으로 로그인한 아이디가 없을 경우
        User user = userRepository.findByNickname(userName)
                .orElseThrow(UserNotFoundException::new);
        // #2 삭제할 포스트가 없을 경우
        Challenge challenge = challengeRepository.findById(postId).orElseThrow(ReviewNotFoundException::new);
        // #3 사용자와 삭제할 포스트의 작성자가 다를 경우
        if (!Objects.equals(challenge.getOwner().getNickname(), user.getNickname()) && !user.getRole().equals(Role.ROLE_ADMIN)) {
            throw new UserNotFoundException();
        }

        challengeRepository.delete(challenge);
        ChallengeResponse challengeResponse = ChallengeResponse.builder().message("게시글 삭제완료").build();
        return challengeResponse;
    }

    public ChallengeDetailResponse get(String userName, Long postId) {
        // #1 토큰으로 로그인한 아이디가 없을 경우
        User user = userRepository.findByNickname(userName)
                .orElseThrow(UserNotFoundException::new);
        // #2 해당 게시글이 존재하지 않을 경우
        Challenge challenge = challengeRepository.findById(postId).orElseThrow(ReviewNotFoundException::new);

        ChallengeDetailResponse postDetailResponse = ChallengeDetailResponse.builder()
                .id(postId)
                .owner(challenge.getOwner())
                .title(challenge.getTitle())
                .description(challenge.getDescription())
                .LastModifiedDatetime(challenge.getLastModifiedDatetime().format(DateTimeFormatter.ofPattern("yyyy-mm-dd hh:mm:ss")))
                .deletedDatetime(challenge.getDeletedDatetime().format(DateTimeFormatter.ofPattern("yyyy-mm-dd hh:mm:ss")))
                .build();
        return postDetailResponse;
    }


    public Page<ChallengeDetailResponse> list(Pageable pageable) {
        Page<Challenge> page = challengeRepository.findAll(pageable);
        Page<ChallengeDetailResponse> challengeDetailResponsePage = ChallengeDetailResponse.toDtoList(page);
        return challengeDetailResponsePage;
    }
}
