package com.example.gread.app.login.service;

import com.example.gread.app.home.domain.ReaderType;
import com.example.gread.app.home.dto.OnboardingResponseDto;
import com.example.gread.app.home.dto.OnboardingRequestDto;
import com.example.gread.app.login.domain.User;
import com.example.gread.app.login.repository.UserRepository;
import com.example.gread.global.code.ErrorCode;
import com.example.gread.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OnboardingService {

    private final UserRepository userRepository;

    @Transactional
    public OnboardingResponseDto updateOnboarding(String subject, OnboardingRequestDto request) {
        User user = findUserBySubject(subject);

        String newNickname = request.getNickname();
        String currentNickname = Optional.ofNullable(user.getProfile())
                .map(p -> p.getNickname())
                .orElse("");

        if (newNickname != null && !newNickname.equals(currentNickname)) {
            if (userRepository.existsByProfileNickname(newNickname)) {
                throw new BusinessException(ErrorCode.DUPLICATE_NICKNAME);
            }
        }

        ReaderType readerType = determineReaderType(request.getReaderType());

        user.completeOnboarding(newNickname, readerType);

        if (request.getPreferenceTags() != null && !request.getPreferenceTags().isEmpty() && user.getProfile() != null) {
            user.getProfile().setPreferenceTags(String.join(", ", request.getPreferenceTags()));
        }

        log.info("### 온보딩 완료 성공: UserEmail = {}, Role = {}", user.getEmail(), user.getRole());

        int recommendedCode = getFirstCategoryCode(readerType);

        return OnboardingResponseDto.builder()
                .readerType(readerType.name())
                .readerTitle(readerType.getTitle())
                .descriptionLines(readerType.getDescriptionLines())
                .recommendedCategoryCode(recommendedCode)
                .build();
    }

    private ReaderType determineReaderType(String typeStr) {
        if (typeStr == null || typeStr.isBlank()) {
            return ReaderType.TYPE_A; // 기본값
        }
        try {
            return ReaderType.valueOf(typeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.warn("### 잘못된 ReaderType 요청 [{}], TYPE_A로 기본 설정합니다.", typeStr);
            return ReaderType.TYPE_A;
        }
    }

    private int getFirstCategoryCode(ReaderType readerType) {
        return Optional.ofNullable(readerType.getCategoryCodes())
                .filter(codes -> !codes.isEmpty())
                .map(codes -> codes.get(0))
                .orElse(0);
    }

    private User findUserBySubject(String subject) {
        if (subject == null || subject.isBlank()) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        try {
            Long userId = Long.parseLong(subject);
            return userRepository.findById(userId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        } catch (NumberFormatException e) {
            return userRepository.findByEmail(subject)
                    .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        }
    }
}