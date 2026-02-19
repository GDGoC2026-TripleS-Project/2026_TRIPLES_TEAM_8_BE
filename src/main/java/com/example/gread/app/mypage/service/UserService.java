package com.example.gread.app.mypage.service;

import com.example.gread.app.mypage.dto.UpdateUserRequest;
import com.example.gread.app.mypage.dto.UserProfileDto;

public interface UserService {
    UserProfileDto getMyProfile(Long userId);
    UserProfileDto updateMyProfile(Long userId, UpdateUserRequest request);
    void deleteMyAccount(Long userId);
}
