package com.example.gread.app.home.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponseDto {
    private Long reviewId;
    private String category;
    private String content;
    private String authorNickname;
    private String authorProfile;
    private String createdAt;
    private String bookTitle;
    private String color;
}