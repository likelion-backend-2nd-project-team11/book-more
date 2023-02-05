package site.bookmore.bookmore.books.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.bookmore.bookmore.alarms.entity.AlarmType;
import site.bookmore.bookmore.books.dto.ReviewPageResponse;
import site.bookmore.bookmore.books.dto.ReviewRequest;
import site.bookmore.bookmore.books.entity.*;
import site.bookmore.bookmore.books.repository.*;
import site.bookmore.bookmore.common.exception.forbidden.InvalidPermissionException;
import site.bookmore.bookmore.common.exception.not_found.BookNotFoundException;
import site.bookmore.bookmore.common.exception.not_found.ReviewNotFoundException;
import site.bookmore.bookmore.common.exception.not_found.ReviewTagRelationNotFound;
import site.bookmore.bookmore.common.exception.not_found.UserNotFoundException;
import site.bookmore.bookmore.observer.event.alarm.AlarmCreate;
import site.bookmore.bookmore.observer.event.alarm.AlarmListCreate;
import site.bookmore.bookmore.users.entity.Follow;
import site.bookmore.bookmore.users.entity.User;
import site.bookmore.bookmore.users.repositroy.FollowRepository;
import site.bookmore.bookmore.users.repositroy.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final BookRepository bookRepository;
    private final FollowRepository followRepository;
    private final LikesRepository likesRepository;

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final ReviewTagRepository reviewTagRepository;
    private final ApplicationEventPublisher publisher;

    // 도서 리뷰 등록
    @Transactional
    public Long create(ReviewRequest reviewRequest, String isbn, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        Book book = bookRepository.findById(isbn)
                .orElseThrow(BookNotFoundException::new);

        Review review = createReview(reviewRequest.toEntity(user, book));

        Set<String> tagsLabel = reviewRequest.getTags();

        if (tagsLabel.isEmpty()) return review.getId();

        // 태그 저장
        for (String tagLabel : tagsLabel) {
            Tag tag = createTagByLabel(tagLabel);
            ReviewTag reviewTag = createReviewTag(review.getId(), tag.getId());
            review.getReviewTags().add(reviewTag);
        }

        // 나의 팔로잉이 리뷰를 등록했을 때의 알림 발생
        List<Follow> follows = followRepository.findAllByFollowingAndDeletedDatetimeIsNull(user);
        List<User> followers = follows.stream().map(Follow::getFollower).collect(Collectors.toList());

        publisher.publishEvent(AlarmListCreate.of(AlarmType.NEW_FOLLOW_REVIEW, followers, user, review.getId()));

        return review.getId();
    }

    // 도서 리뷰 조회
    @Transactional
    public Page<ReviewPageResponse> read(Pageable pageable, String isbn) {
        Book book = bookRepository.findById(isbn)
                .orElseThrow(BookNotFoundException::new);

        return reviewRepository.findByBookAndDeletedDatetimeIsNull(pageable, book).map(ReviewPageResponse::of);
    }

    // 도서 리뷰 수정
    @Transactional
    public Long update(ReviewRequest reviewRequest, Long reviewId, String email) {
        Review review = readReviewWithTag(reviewId);

        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        if (!Objects.equals(review.getAuthor().getEmail(), user.getEmail())) {
            throw new InvalidPermissionException();
        }

        Set<String> updateTagsLabel = reviewRequest.getTags();

        review.update(reviewRequest.toEntity());

        if (updateTagsLabel.isEmpty()) return review.getId();

        Set<Tag> oldTags = review.getTags();

        Set<Tag> updateTags = new HashSet<>();
        for (String label : updateTagsLabel) {
            updateTags.add(createTagByLabel(label));
        }

        // 차집합 : 제거해야할 태그
        oldTags.removeAll(updateTags);

        // 태그 제거
        for (Tag tag : oldTags) {
            ReviewTag reviewTag = readReviewTag(review.getId(), tag.getId());
            review.removeReviewTag(reviewTag); // review 객체에서 관계 삭제
            deleteReviewTag(reviewTag); // DB에서 관계 삭제
        }

        // 새로운 태그 관계 저장
        for (Tag tag : updateTags) {
            createReviewTag(review, tag);
        }

        return review.getId();
    }

    // 도서 리뷰 삭제
    @Transactional
    public Long delete(Long reviewId, String email) {
        Review review = reviewRepository.findByIdAndDeletedDatetimeIsNull(reviewId)
                .orElseThrow(ReviewNotFoundException::new);

        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        if (!Objects.equals(review.getAuthor().getEmail(), user.getEmail())) {
            throw new InvalidPermissionException();
        }

        review.delete();

        return review.getId();
    }

    // 도서 리뷰에 좋아요 | 취소
    @Transactional
    public boolean doLikes(String email, Long reviewId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        Review review = reviewRepository.findByIdAndDeletedDatetimeIsNull(reviewId)
                .orElseThrow(ReviewNotFoundException::new);

        Likes likes = likesRepository.findByUserAndReview(user, review)
                .orElse(Likes.of(user, review));

        boolean result = likes.likes();

        likesRepository.save(likes);

        // 내가 작성한 리뷰에 좋아요가 달렸을 때의 알림 발생
        if (likes.isLiked()) {
            publisher.publishEvent(AlarmCreate.of(AlarmType.NEW_LIKE_ON_REVIEW, review.getAuthor(), user, likes.getId()));
        }

        return result;
    }

    private Review createReview(Review review) {
        return reviewRepository.save(review);
    }

    private Tag createTagByLabel(String label) {
        // 기존에 존재하는 태그는 검색하여 반환
        // 새로운 태그는 저장하고 반환
        return tagRepository.findByLabel(label).orElseGet(() -> tagRepository.save(Tag.of(label)));
    }

    private ReviewTag createReviewTag(Long reviewId, Long tagId) {
        ReviewTag reviewTag = ReviewTag.of(
                reviewRepository.getReferenceById(reviewId),
                tagRepository.getReferenceById(tagId)
        );

        return reviewTagRepository.save(reviewTag);
    }

    private ReviewTag createReviewTag(Review review, Tag tag) {
        return reviewTagRepository.findByReviewAndTag(review, tag)
                .orElseGet(() -> reviewTagRepository.save(ReviewTag.of(review, tag)));
    }

    private Review readReviewWithTag(Long id) {
        return reviewRepository.findByIdWithTags(id).orElseThrow(ReviewNotFoundException::new);
    }

    private ReviewTag readReviewTag(Long reviewId, Long tagId)  {
        return reviewTagRepository.findByReviewAndTag(
                reviewRepository.getReferenceById(reviewId),
                tagRepository.getReferenceById(tagId)
        ).orElseThrow(ReviewTagRelationNotFound::new);
    }

    private void deleteReviewTag(ReviewTag reviewTag) {
        reviewTagRepository.delete(reviewTag);
    }
}