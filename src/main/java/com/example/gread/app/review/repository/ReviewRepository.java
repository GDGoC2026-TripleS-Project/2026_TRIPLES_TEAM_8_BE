package com.example.gread.app.review.repository;

import com.example.gread.app.login.domain.Profile;
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
    Page<Review> findByProfile(Profile profile, Pageable pageable);
    @Query(value = "SELECT * FROM review r " +
            "WHERE r.minor_code IN :codes " +
            "ORDER BY RAND() LIMIT 5", nativeQuery = true)
    List<Review> findRandomByCategoryCodes(@Param("codes") List<String> codes);

    @Query(value = "SELECT * FROM review r ORDER BY RAND() LIMIT 5", nativeQuery = true)
    List<Review> findRandom5();

    List<Review> findByProfileId(Long profileId);

    List<Review> findByBookId(Long bookId);

    Optional<Review> findByReviewIdAndProfileId(Long reviewId, Long profileId);

    List<Review> findAllByOrderByCreatedAtDesc();

    Long countByBookId(Long bookId);

    Long countByProfileId(Long profileId);

    Page<Review> findByUser(User user, Pageable pageable);

    Page<Review> findByProfileId(Long profileId, Pageable pageable);

    // 프로필별 리뷰 개수 집계 (내림차순 정렬)
    @Query("SELECT r.profile, COUNT(r) FROM Review r GROUP BY r.profile ORDER BY COUNT(r) DESC")
    List<Object[]> countReviewsByProfile();
}
