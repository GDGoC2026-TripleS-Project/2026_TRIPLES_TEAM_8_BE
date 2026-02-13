package com.example.gread.app.mypage.service;

import com.example.gread.app.mypage.dto.ReviewDto;
import com.example.gread.app.mypage.dto.UpdateUserRequest;
import com.example.gread.app.mypage.dto.UserProfileDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserProfileDto getMyProfile(String email);
    UserProfileDto updateMyProfile(String email, UpdateUserRequest request);
    void deleteMyAccount(String email);
    Page<ReviewDto> getMyReviews(String email, Pageable pageable);
}
