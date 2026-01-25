package com.example.gread.login.repository;

import com.example.gread.login.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    // 특정 사용자가 쓴 리뷰만 모두 가져오는 메서드
    List<Review> findByUserId(Long userId);
}