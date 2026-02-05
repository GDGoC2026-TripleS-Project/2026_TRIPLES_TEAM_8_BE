package com.example.gread.app.review.service;

import com.example.gread.app.review.domain.Review;
import com.example.gread.app.review.dto.ReviewReqDto;
import com.example.gread.app.review.dto.ReviewResDto;
import com.example.gread.app.review.repository.ReviewRepository;
import com.example.gread.global.code.ErrorCode;
import com.example.gread.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Transactional
    public ReviewResDto postReview(ReviewReqDto dto, Long userId, Long bookId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND_EXCEPTION));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BOOK_NOT_FOUND_EXCEPTION));

        Review review = new Review(
                user,
                book,
                dto.getReviewColor(),
                dto.getReviewContent()
        );

        Review savedReview = reviewRepository.save(review);

        return ReviewResDto.from(savedReview);
    }

    @Transactional(readOnly = true)
    public List<ReviewResDto> findReviewByUserId(Long userId) {

        return reviewRepository.findByUserId(userId)
                .stream()
                .map(ReviewResDto::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ReviewResDto> findReviewByBookId(Long bookId) {

        return reviewRepository.findByBookId(bookId)
                .stream()
                .map(ReviewResDto::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ReviewResDto> findLatestReviewsByBookId(Long bookId) {
        List<Review> reviews = reviewRepository.findByBookIdOrderByCreatedAtDesc(bookId);
        return reviews.stream()
                .map(ReviewResDto::from)
                .toList();
    }

    @Transactional
    public ReviewResDto updateReview(ReviewReqDto dto, Long reviewId, Long userId) {

        Review review = reviewRepository
                .findByReviewId(reviewId, userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.REVIEW_NOT_FOUND_EXCEPTION));

        review.update(
                dto.getReviewColor(),
                dto.getReviewContent()
        );

        return ReviewResDto.from(review);
    }

    @Transactional
    public void deleteReviewById(Long reviewId, Long userId) {

        Review review = reviewRepository
                .findByReviewId(reviewId, userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.REVIEW_NOT_FOUND_EXCEPTION));

        reviewRepository.delete(review);
    }
}