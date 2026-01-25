package com.example.gread.login.service;

import com.example.gread.login.domain.Book;
import com.example.gread.login.domain.Review;
import com.example.gread.login.domain.User;
import com.example.gread.login.repository.BookRepository;
import com.example.gread.login.repository.ReviewRepository;
import com.example.gread.login.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    /**
     * 리뷰 작성
     */
    @Transactional
    public Long writeReview(Long userId, Long bookId, String content) {
        // 엔티티 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("도서를 찾을 수 없습니다."));

        // 리뷰 생성
        Review review = Review.builder()
                .user(user)
                .book(book)
                .content(content)
                .build();

        return reviewRepository.save(review).getId();
    }

    /**
     * 특정 사용자의 모든 리뷰 조회
     */
    public List<Review> findReviewsByUser(Long userId) {
        return reviewRepository.findByUserId(userId);
    }
}