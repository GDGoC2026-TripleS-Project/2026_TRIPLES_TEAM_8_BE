package com.example.gread.login.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDto {
    private String email;
    private String nickname;
}