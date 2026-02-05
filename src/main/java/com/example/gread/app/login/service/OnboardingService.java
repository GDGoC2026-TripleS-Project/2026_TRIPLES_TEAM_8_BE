package com.example.gread.app.login.service;

import com.example.gread.app.login.domain.Profile;
import com.example.gread.app.login.domain.User;
import com.example.gread.app.login.repository.ProfileRepository;
import com.example.gread.app.login.repository.UserRepository;
import com.example.gread.global.code.ErrorCode;
import com.example.gread.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional // 데이터 저장을 위해 트랜잭션 처리
public class OnboardingService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    public void updateProfile(String email, Map<String, String> request) {
        // 1. 유저 찾기
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 2. 프로필 가져오기 (만약 Profile이 null일 경우를 대비한 로직이 도메인에 있다면 더 좋음)
        Profile profile = user.getProfile();
        if (profile == null) {
            // 프로필이 없는 유저일 경우의 예외 처리 (필요시)
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        // 3. 요청 데이터로 프로필 수정
        profile.setNickname(request.get("nickname"));
        profile.setTestResultCode(request.get("testResultCode"));
        profile.setPreferenceTags(request.get("tags"));

        // 4. 저장 (Dirty Checking 덕분에 save 생략 가능하나 명시적으로 작성해도 됨)
        profileRepository.save(profile);
    }
}