package site.bookmore.bookmore.books.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.bookmore.bookmore.books.dto.ReviewRequest;
import site.bookmore.bookmore.books.entity.Book;
import site.bookmore.bookmore.books.entity.Review;
import site.bookmore.bookmore.books.repository.BookRepository;
import site.bookmore.bookmore.books.repository.ReviewRepository;
import site.bookmore.bookmore.common.exception.AbstractAppException;
import site.bookmore.bookmore.common.exception.ErrorCode;
import site.bookmore.bookmore.users.entity.User;
import site.bookmore.bookmore.users.repositroy.UserRepository;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final BookRepository bookRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    // 도서 리뷰 등록
    public Long create(ReviewRequest reviewRequest, String isbn, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AbstractAppException(ErrorCode.EMAIL_NOT_FOUND) {
                });

        Book book = bookRepository.findById(isbn)
                .orElseThrow(() -> new AbstractAppException(ErrorCode.BOOK_NOT_FOUND) {
                });

        Review review = reviewRequest.toEntity(user, book);
        Review savedReview = reviewRepository.save(review);

        return savedReview.getId();
    }
}