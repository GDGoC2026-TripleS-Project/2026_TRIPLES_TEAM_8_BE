package com.example.gread.app.review.repository;

import com.example.gread.app.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByUserId(Long userId);

    List<Review> findByBookId(Long bookId);

    Optional<Review> findByReviewId(Long reviewId, Long userId);

    List<Review> findByBookIdOrderByCreatedAtDesc(Long bookId);
}