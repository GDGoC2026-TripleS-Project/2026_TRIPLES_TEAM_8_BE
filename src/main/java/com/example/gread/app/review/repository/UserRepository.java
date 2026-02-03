package com.example.gread.app.review.repository;

import com.example.gread.app.review.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}