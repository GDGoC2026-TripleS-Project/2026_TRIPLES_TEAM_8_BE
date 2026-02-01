package com.example.gread.app.review.service;

import com.example.gread.app.review.domain.Book;
import com.example.gread.app.review.domain.Review;
import com.example.gread.app.review.domain.User;
import com.example.gread.app.review.dto.ReviewReqDto;
import com.example.gread.app.review.dto.ReviewResDto;
import com.example.gread.app.review.repository.BookRepository;
import com.example.gread.app.review.repository.ReviewRepository;
import com.example.gread.app.review.repository.UserRepository;
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
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("해당 책을 찾을 수 없습니다."));

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
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰를 찾을 수 없습니다."));

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
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰를 찾을 수 없습니다."));

        reviewRepository.delete(review);
    }
}
