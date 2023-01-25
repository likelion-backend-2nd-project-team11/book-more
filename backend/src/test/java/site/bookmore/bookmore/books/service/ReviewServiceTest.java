package site.bookmore.bookmore.books.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationEventPublisher;
import site.bookmore.bookmore.books.dto.ReviewRequest;
import site.bookmore.bookmore.books.entity.Book;
import site.bookmore.bookmore.books.entity.Chart;
import site.bookmore.bookmore.books.entity.Likes;
import site.bookmore.bookmore.books.entity.Review;
import site.bookmore.bookmore.books.repository.BookRepository;
import site.bookmore.bookmore.books.repository.LikesRepository;
import site.bookmore.bookmore.books.repository.ReviewRepository;
import site.bookmore.bookmore.common.exception.AbstractAppException;
import site.bookmore.bookmore.common.exception.ErrorCode;
import site.bookmore.bookmore.users.entity.User;
import site.bookmore.bookmore.users.repositroy.FollowRepository;
import site.bookmore.bookmore.users.repositroy.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ReviewServiceTest {

    private final BookRepository bookRepository = Mockito.mock(BookRepository.class);
    private final FollowRepository followRepository = Mockito.mock(FollowRepository.class);
    private final LikesRepository likesRepository = Mockito.mock(LikesRepository.class);
    private final ReviewRepository reviewRepository = Mockito.mock(ReviewRepository.class);
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final ApplicationEventPublisher publisher = Mockito.mock(ApplicationEventPublisher.class);
    private final ReviewService reviewService = new ReviewService(bookRepository, followRepository, likesRepository, reviewRepository, userRepository, publisher);

    private final User user = User.builder()
            .email("email")
            .build();

    private final User user2 = User.builder()
            .email("email2")
            .build();

    private final Book book = Book.builder()
            .id("isbn")
            .build();

    private final Review review = Review.builder()
            .author(user)
            .book(book)
            .build();

    private final Likes likes = Likes.builder()
            .liked(true)
            .review(review)
            .user(user)
            .build();

    /* ========== 도서 리뷰 등록 ========== */
    @Test
    @DisplayName("도서 리뷰 등록 성공")
    void create_success() {
        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));
        when(bookRepository.findById(book.getId()))
                .thenReturn(Optional.of(book));
        when(reviewRepository.save(any(Review.class)))
                .thenReturn(review);

        Assertions.assertDoesNotThrow(() -> reviewService.create(new ReviewRequest(), book.getId(), user.getEmail()));
    }

    @Test
    @DisplayName("도서 리뷰 등록 실패 - 유저 정보가 없는 경우")
    void create_user_not_found() {
        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.empty());
        when(bookRepository.findById(book.getId()))
                .thenReturn(Optional.of(book));
        when(reviewRepository.save(any(Review.class)))
                .thenReturn(review);

        AbstractAppException abstractAppException = Assertions.assertThrows(AbstractAppException.class, () -> reviewService.create(new ReviewRequest(), book.getId(), user.getEmail()));
        assertEquals(ErrorCode.USER_NOT_FOUND, abstractAppException.getErrorCode());
    }

    @Test
    @DisplayName("도서 리뷰 등록 실패 - 책 정보가 없는 경우")
    void create_book_not_found() {
        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));
        when(bookRepository.findById(book.getId()))
                .thenReturn(Optional.empty());
        when(reviewRepository.save(any(Review.class)))
                .thenReturn(review);

        AbstractAppException abstractAppException = Assertions.assertThrows(AbstractAppException.class, () -> reviewService.create(new ReviewRequest(), book.getId(), user.getEmail()));
        assertEquals(ErrorCode.BOOK_NOT_FOUND, abstractAppException.getErrorCode());
    }

    /* ========== 도서 리뷰 수정 ========== */
    @Test
    @DisplayName("도서 리뷰 수정 실패 - 해당 리뷰가 없는 경우")
    void update_review_not_found() {
        when(reviewRepository.findById(review.getId()))
                .thenReturn(Optional.empty());

        AbstractAppException abstractAppException = Assertions.assertThrows(AbstractAppException.class, () -> reviewService.update(new ReviewRequest(), review.getId(), user.getEmail()));
        assertEquals(ErrorCode.REVIEW_NOT_FOUND, abstractAppException.getErrorCode());
    }

    @Test
    @DisplayName("도서 리뷰 수정 실패 - 해당 유저가 없는 경우")
    void update__user_not_found() {
        when(reviewRepository.findById(review.getId()))
                .thenReturn(Optional.of(review));
        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.empty());

        AbstractAppException abstractAppException = Assertions.assertThrows(AbstractAppException.class, () -> reviewService.update(new ReviewRequest(), review.getId(), user.getEmail()));
        assertEquals(ErrorCode.USER_NOT_FOUND, abstractAppException.getErrorCode());
    }

    @Test
    @DisplayName("도서 리뷰 수정 실패 - 작성자와 유저가 일치하지 않는 경우")
    void update_invalid_permission() {
        when(reviewRepository.findById(review.getId()))
                .thenReturn(Optional.of(review));
        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));
        when(userRepository.findByEmail(user2.getEmail()))
                .thenReturn(Optional.of(user2));

        AbstractAppException abstractAppException = Assertions.assertThrows(AbstractAppException.class, () -> reviewService.update(new ReviewRequest("new body", true, new Chart()), review.getId(), user2.getEmail()));
        assertEquals(ErrorCode.INVALID_PERMISSION, abstractAppException.getErrorCode());
    }

    /* ========== 도서 리뷰 삭제 ========== */
    @Test
    @DisplayName("도서 리뷰 삭제 실패 - 해당 리뷰가 없는 경우")
    void delete_review_not_found() {
        when(reviewRepository.findById(review.getId()))
                .thenReturn(Optional.empty());

        AbstractAppException abstractAppException = Assertions.assertThrows(AbstractAppException.class, () -> reviewService.delete(review.getId(), user.getEmail()));
        assertEquals(ErrorCode.REVIEW_NOT_FOUND, abstractAppException.getErrorCode());
    }

    @Test
    @DisplayName("도서 리뷰 삭제 실패 - 해당 유저가 없는 경우")
    void delete__user_not_found() {
        when(reviewRepository.findById(review.getId()))
                .thenReturn(Optional.of(review));
        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.empty());

        AbstractAppException abstractAppException = Assertions.assertThrows(AbstractAppException.class, () -> reviewService.delete(review.getId(), user.getEmail()));
        assertEquals(ErrorCode.USER_NOT_FOUND, abstractAppException.getErrorCode());
    }

    @Test
    @DisplayName("도서 리뷰 삭제 실패 - 작성자와 유저가 일치하지 않는 경우")
    void delete_invalid_permission() {
        when(reviewRepository.findById(review.getId()))
                .thenReturn(Optional.of(review));
        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));
        when(userRepository.findByEmail(user2.getEmail()))
                .thenReturn(Optional.of(user2));

        AbstractAppException abstractAppException = Assertions.assertThrows(AbstractAppException.class, () -> reviewService.delete(review.getId(), user2.getEmail()));
        assertEquals(ErrorCode.INVALID_PERMISSION, abstractAppException.getErrorCode());
    }

    /* ========== 도서 리뷰 좋아요 | 취소 ========== */
    @Test
    @DisplayName("도서 리뷰 좋아요 성공")
    void doLikes_success() {
        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));
        when(reviewRepository.findById(review.getId()))
                .thenReturn(Optional.of(review));
        when(likesRepository.findByUserAndReview(user, review))
                .thenReturn(Optional.empty());

        boolean result = reviewService.doLikes(user.getEmail(), review.getId());

        assertTrue(result);
        assertEquals(1, review.getLikesCount());
    }

    @Test
    @DisplayName("도서 리뷰 좋아요 취소 성공")
    void doLikes_cancel_success() {
        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));
        when(reviewRepository.findById(review.getId()))
                .thenReturn(Optional.of(review));
        when(likesRepository.findByUserAndReview(user, review))
                .thenReturn(Optional.of(likes));

        boolean result = reviewService.doLikes(user.getEmail(), review.getId());

        assertFalse(result);
        assertEquals(0, review.getLikesCount());
    }
}