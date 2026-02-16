package com.example.gread.app.home.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ReviewResponseDto {
    private Long reviewId;
    private String category;
    private String content;
    private String authorNickname;
    private String createdAt;
    private String bookTitle;
}