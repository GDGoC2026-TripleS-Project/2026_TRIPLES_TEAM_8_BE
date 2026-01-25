package com.example.gread.login.controller;

import com.example.gread.login.domain.Profile;
import com.example.gread.login.domain.User;
import com.example.gread.login.repository.ProfileRepository;
import com.example.gread.login.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/onboarding")
public class OnboardingController {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    /**
     * 심리테스트 결과 및 취향 저장
     */
    @PostMapping("/result/{userId}")
    public ResponseEntity<String> saveOnboardingResult(
            @PathVariable Long userId,
            @RequestBody OnboardingRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

        // 기존 프로필이 있으면 수정, 없으면 신규 생성
        Profile profile = user.getProfile();
        if (profile == null) {
            profile = new Profile();
            profile.setUser(user);
        }

        profile.setNickname(request.getNickname());
        profile.setTestResultCode(request.getTestResultCode()); // 8가지 유형
        profile.setPreferenceTags(request.getPreferenceTags()); // 관심 키워드 2개

        profileRepository.save(profile);
        return ResponseEntity.ok("취향 결과 저장 완료");
    }

    @Data
    static class OnboardingRequest {
        private String nickname;
        private String testResultCode;
        private String preferenceTags;
    }
}