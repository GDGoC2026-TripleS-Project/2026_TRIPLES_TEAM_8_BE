package com.example.gread.app.mypage.service;

import com.example.gread.app.mypage.dto.ReviewDto;
import com.example.gread.app.mypage.dto.UpdateUserRequest;
import com.example.gread.app.mypage.dto.UserProfileDto;
import com.example.gread.app.review.domain.Review;
import com.example.gread.app.login.domain.User;
import com.example.gread.app.review.repository.ReviewRepository;
import com.example.gread.app.login.repository.UserRepository;
import com.example.gread.global.code.ErrorCode;
import com.example.gread.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public UserProfileDto getMyProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        return toDto(user);
    }

    @Override
    public UserProfileDto updateMyProfile(Long userId, UpdateUserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        
        if (user.getProfile() != null) {
            if (request.getNickname() != null) {
                user.getProfile().setNickname(request.getNickname());
            }
            if (request.getProfileImageUrl() != null) {
                user.getProfile().setProfileImageUrl(request.getProfileImageUrl());
            }
        }
        return toDto(user);
    }

    @Override
    public void deleteMyAccount(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        userRepository.delete(user);
    }

    @Override
    public Page<ReviewDto> getMyReviews(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        Page<Review> reviews = reviewRepository.findByUser(user, pageable);
        return reviews.map(r -> ReviewDto.builder()
                .reviewId(r.getReviewId())
                .title(r.getTitle())
                .content(r.getReviewContent())
                .build());
    }

    private UserProfileDto toDto(User user) {
        return UserProfileDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getProfile() != null ? user.getProfile().getNickname() : null)
                .readerType(user.getReaderType() != null ? user.getReaderType().name() : null)
                .profileImageUrl(user.getProfile() != null ? user.getProfile().getProfileImageUrl() : null)
                .build();
    }
}
