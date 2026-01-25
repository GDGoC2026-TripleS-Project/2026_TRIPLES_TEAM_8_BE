package com.example.gread.global.responseTemplate;

import com.example.gread.login.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    // 기본적인 CRUD 기능이 모두 포함
}