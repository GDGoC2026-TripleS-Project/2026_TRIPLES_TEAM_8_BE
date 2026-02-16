package com.example.gread.app.login.repository;

import com.example.gread.app.login.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByGoogleSub(String googleSub);

    @Query("SELECT COUNT(u) > 0 FROM User u JOIN u.profile p WHERE p.nickname = :nickname")
    boolean existsByProfileNickname(@Param("nickname") String nickname);}