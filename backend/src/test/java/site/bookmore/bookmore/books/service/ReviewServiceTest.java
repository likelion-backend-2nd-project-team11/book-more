package site.bookmore.bookmore.books.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import site.bookmore.bookmore.books.dto.ReviewRequest;
import site.bookmore.bookmore.books.entity.Book;
import site.bookmore.bookmore.books.entity.Review;
import site.bookmore.bookmore.books.repository.BookRepository;
import site.bookmore.bookmore.books.repository.ReviewRepository;
import site.bookmore.bookmore.common.exception.AbstractAppException;
import site.bookmore.bookmore.common.exception.ErrorCode;
import site.bookmore.bookmore.users.entity.User;
import site.bookmore.bookmore.users.repositroy.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ReviewServiceTest {

    private final BookRepository bookRepository = Mockito.mock(BookRepository.class);
    private final ReviewRepository reviewRepository = Mockito.mock(ReviewRepository.class);
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final ReviewService reviewService = new ReviewService(bookRepository, reviewRepository, userRepository);

    /* ========== 도서 리뷰 등록 ========== */
    private final User user = User.builder()
            .email("email")
            .build();

    private final Book book = Book.builder()
            .id("isbn")
            .build();

    private final Review review = Review.builder()
            .author(user)
            .book(book)
            .build();

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
}