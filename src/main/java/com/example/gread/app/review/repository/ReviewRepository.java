package com.example.gread.app.review.repository;

import com.example.gread.app.login.domain.User;
import com.example.gread.app.review.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // 1. 회원용: 특정 카테고리 내에서 랜덤 추출
    @Query("SELECT r FROM Review r " +
            "LEFT JOIN FETCH r.book b " +
            "LEFT JOIN FETCH r.user u " +
            "WHERE r.category IN :codes " +  // r.category로 직접 필터링
            "ORDER BY function('RAND')")
    List<Review> findRandomByCategoryCodes(@Param("codes") List<String> codes, Pageable pageable);

    // 3. 비회원용: 전체 리뷰 중 랜덤 5개
    @Query("SELECT r FROM Review r " +
            "LEFT JOIN FETCH r.book b " +
            "LEFT JOIN FETCH r.user u " +
            "ORDER BY function('RAND')")
    List<Review> findRandom5(Pageable pageable);

    List<Review> findByProfileId(Long profileId);

    List<Review> findByBookId(Long bookId);

    Optional<Review> findByReviewId(Long reviewId, Long userId);

    List<Review> findByBookIdOrderByCreatedAtDesc(Long bookId);

    Long countByBookId(Long bookId);

    Long countByProfileId(Long bookId);

    Page<Review> findByUser(User user, Pageable pageable);
}
