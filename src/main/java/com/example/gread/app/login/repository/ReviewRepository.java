package com.example.gread.app.login.repository;

import com.example.gread.app.login.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query(value = "SELECT * FROM review ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    List<Review> findRandomReviews(@Param("limit") int limit);

    @Query(value = "SELECT * FROM review WHERE content LIKE %:tag% ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    List<Review> findRecommendedReviews(@Param("tag") String tag, @Param("limit") int limit);
}