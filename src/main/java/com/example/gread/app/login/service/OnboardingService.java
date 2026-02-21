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
            profile = Profile.builder()
                    .user(user)
                    .id(user.getId())
                    .nickname("GUEST_" + java.util.UUID.randomUUID().toString().substring(0, 8))
                    .build();
            profileRepository.save(profile);
            user.setProfile(profile);
        }

        if (!request.getNickname().equals(profile.getNickname()) &&
                profileRepository.existsByNickname(request.getNickname())) {
            throw new BusinessException(ErrorCode.DUPLICATE_NICKNAME);
        }

        profile.setNickname(request.getNickname());
        profile.setPreferenceTags(request.getPreferenceTags());

        ReaderType readerType;
        try {
            String typeStr = (request.getReaderType() != null) ? request.getReaderType() : "TYPE_A";
            readerType = ReaderType.valueOf(typeStr.toUpperCase());
            profile.setReaderType(readerType);
        } catch (IllegalArgumentException e) {
            readerType = ReaderType.TYPE_A;
            profile.setReaderType(readerType);
        }

        user.upgradeToUser();

        log.info("### 온보딩 완료 - User: {}, Nickname: {}, Type: {}", user.getId(), profile.getNickname(), readerType);

        return OnboardingResponseDto.builder()
                .readerType(readerType.name())
                .readerTitle(readerType.getTitle())
                .descriptionLines(readerType.getDescriptionLines())
                .recommendedCategoryCode(readerType.getMinorCodes().get(0))
                .build();
    }
}
