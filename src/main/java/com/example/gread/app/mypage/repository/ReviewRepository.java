package com.example.gread.app.mypage.repository;

import com.example.gread.app.mypage.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.gread.app.mypage.entity.User;
import org.springframework.data.domain.Pageable;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findByUser(User user, Pageable pageable);
}
