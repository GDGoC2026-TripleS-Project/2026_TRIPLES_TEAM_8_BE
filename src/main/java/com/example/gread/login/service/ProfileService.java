package com.example.gread.login.service;

import com.example.gread.login.domain.Profile;
import com.example.gread.login.domain.User;
import com.example.gread.login.repository.ProfileRepository;
import com.example.gread.login.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    /**
     * 프로필 생성 및 업데이트
     */
    @Transactional
    public Long createProfile(Long userId, String nickname, String testResult) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Profile profile = Profile.builder()
                .nickname(nickname)
                .testResultCode(testResult)
                .user(user)
                .build();

        // User 엔티티와의 연관관계 설정
        user.setProfile(profile);

        return profileRepository.save(profile).getId();
    }
}