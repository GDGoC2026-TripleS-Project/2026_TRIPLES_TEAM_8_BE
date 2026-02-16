package com.example.gread.app.mypage.service;

import com.example.gread.app.review.dto.ReviewResDto;
import com.example.gread.app.mypage.dto.UpdateUserRequest;
import com.example.gread.app.mypage.dto.UserProfileDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserProfileDto getMyProfile(Long userId);
    UserProfileDto updateMyProfile(Long userId, UpdateUserRequest request);
    void deleteMyAccount(Long userId);
    Page<ReviewResDto> getMyReviews(Long userId, Pageable pageable);
}
