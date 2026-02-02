package com.example.gread.login.repository;

import com.example.gread.login.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    // userId로 토큰 존재 여부 확인
    Optional<RefreshToken> findByUserId(Long userId);

    // 토큰 값 자체로 조회 (재발급 검증 시 사용)
    Optional<RefreshToken> findByTokenValue(String tokenValue);
}