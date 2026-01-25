package com.example.gread.login.repository;

import com.example.gread.login.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // 이메일로 사용자를 찾는 메서드 (로그인 시 필요)
    Optional<User> findByEmail(String email);
}