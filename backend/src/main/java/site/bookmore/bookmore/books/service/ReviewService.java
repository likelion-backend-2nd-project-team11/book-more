package site.bookmore.bookmore.books.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.bookmore.bookmore.books.dto.ReviewRequest;
import site.bookmore.bookmore.books.entity.Book;
import site.bookmore.bookmore.books.entity.Likes;
import site.bookmore.bookmore.books.entity.Review;
import site.bookmore.bookmore.books.repository.BookRepository;
import site.bookmore.bookmore.books.repository.LikesRepository;
import site.bookmore.bookmore.books.repository.ReviewRepository;
import site.bookmore.bookmore.common.exception.not_found.BookNotFoundException;
import site.bookmore.bookmore.common.exception.not_found.ReviewNotFoundException;
import site.bookmore.bookmore.common.exception.not_found.UserNotFoundException;
import site.bookmore.bookmore.users.entity.User;
import site.bookmore.bookmore.users.repositroy.UserRepository;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final BookRepository bookRepository;
    private final LikesRepository likesRepository;

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    // 도서 리뷰 등록
    public Long create(ReviewRequest reviewRequest, String isbn, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        Book book = bookRepository.findById(isbn)
                .orElseThrow(BookNotFoundException::new);

        Review review = reviewRequest.toEntity(user, book);
        Review savedReview = reviewRepository.save(review);

        // 나의 팔로잉이 리뷰를 등록했을 때의 알림 발생 추가해야 함

        return savedReview.getId();
    }

    // 도서 리뷰에 좋아요 | 취소
    public boolean doLikes(String email, Long reviewId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(ReviewNotFoundException::new);

        Likes likes = likesRepository.findByUserAndReview(user, review)
                .orElse(Likes.of(user, review));

        boolean result = likes.likes();

        likesRepository.save(likes);

        // 내가 작성한 리뷰에 좋아요가 달렸을 때의 알림 발생 추가해야 함

        return result;
    }
}