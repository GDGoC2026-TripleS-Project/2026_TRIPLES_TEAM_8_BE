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
    private Long profileId;
    private Long bookId;
    private String nickname;
    private String reviewColor;
    private String reviewContent;
    private String category;
    private String createdAt;
    private String updatedAt;
    private Long createdTimeAgo;
}