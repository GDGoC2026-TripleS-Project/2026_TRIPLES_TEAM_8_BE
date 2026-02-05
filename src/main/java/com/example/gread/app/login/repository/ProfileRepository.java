package com.example.gread.app.login.repository;

import com.example.gread.app.login.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    // 기본적인 CRUD 기능이 모두 포함되어 있습니다.
}