package com.example.gread.app.login.repository;

import com.example.gread.app.login.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    boolean existsByNickname(String nickname);
}