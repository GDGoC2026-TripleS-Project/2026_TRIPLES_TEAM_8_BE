package com.example.gread.app.login.service;

import com.example.gread.app.home.domain.ReaderType;
import com.example.gread.app.home.dto.OnboardingResponseDto;
import com.example.gread.app.login.domain.Profile;
import com.example.gread.app.login.domain.User;
import com.example.gread.app.login.dto.OnboardingRequestDto;
import com.example.gread.app.login.repository.ProfileRepository;
import com.example.gread.app.login.repository.UserRepository;
import com.example.gread.global.code.ErrorCode;
import com.example.gread.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OnboardingService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    @Transactional
    public OnboardingResponseDto updateOnboarding(String subject, OnboardingRequestDto request) {
        User user;
        try {
            Long userId = Long.parseLong(subject);
            user = userRepository.findById(userId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        } catch (NumberFormatException e) {
            user = userRepository.findByEmail(subject)
                    .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        }

        Profile profile = user.getProfile();
        if (profile == null) {
            log.info("### Profile이 없어 새로 생성합니다. User ID: {}", user.getId());
            profile = Profile.builder()
                    .user(user)
                    .nickname("GUEST_" + java.util.UUID.randomUUID().toString().substring(0, 8))
                    .build();
            profileRepository.save(profile);
            user.setProfile(profile);
        }

        if (user.getReaderType() != null && profile.getNickname() != null) {
            log.info("### 이미 온보딩을 완료한 유저입니다. User ID: {}", user.getId());
        }

        if (!request.getNickname().equals(profile.getNickname()) &&
                userRepository.existsByProfileNickname(request.getNickname())) {
            throw new BusinessException(ErrorCode.DUPLICATE_NICKNAME);
        }

        profile.setNickname(request.getNickname());
        profile.setTestResultCode(request.getTestResultCode());
        profile.setPreferenceTags(request.getPreferenceTags());

        try {
            String typeStr = request.getReaderType() != null ? request.getReaderType() : "TYPE_A";
            user.setReaderType(ReaderType.valueOf(typeStr.toUpperCase()));
        } catch (IllegalArgumentException e) {
            log.warn("### 유효하지 않은 ReaderType: {}. 기본값 TYPE_A로 설정합니다.", request.getReaderType());
            user.setReaderType(ReaderType.TYPE_A);
        }

        log.info("### 온보딩 정보 업데이트 성공. User ID: {}", user.getId());

        return null;
    }
}