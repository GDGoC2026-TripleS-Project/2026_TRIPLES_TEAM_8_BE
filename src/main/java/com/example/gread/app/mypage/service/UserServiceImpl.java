package com.example.gread.app.mypage.service;

import com.example.gread.app.review.dto.ReviewResDto;
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
    public Page<ReviewResDto> getMyReviews(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        
        if (user.getProfile() == null) {
            return Page.empty(pageable);
        }
        
        Page<Review> reviews = reviewRepository.findByProfileId(user.getProfile().getId(), pageable);
        return reviews.map(r -> ReviewResDto.builder()
                .reviewId(r.getReviewId())
                .reviewContent(r.getReviewContent())
                .build());
    }

    private UserProfileDto toDto(User user) {
        Long reviewCount = 0L;
        if (user.getProfile() != null) {
            reviewCount = reviewRepository.countByProfileId(user.getProfile().getId());
        }

        return UserProfileDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getProfile() != null ? user.getProfile().getNickname() : null)
                .readerType(user.getReaderType() != null ? user.getReaderType().name() : null)
                .reviewCount(reviewCount)
                .build();
    }
}
