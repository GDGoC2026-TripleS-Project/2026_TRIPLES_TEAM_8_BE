package com.example.gread.app.review.dto;

import com.example.gread.app.review.domain.Review;
import com.example.gread.app.review.domain.ReviewColor;
import lombok.Builder;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;

@Builder
@Getter
public class ReviewResDto {

    private Long reviewId;
    private Long userId;
    private Long bookId;

    private String nickname;
    private String profileImageUrl;
    private ReviewColor reviewColor;
    private String reviewContent;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private long createdTimeAgo;

    public static ReviewResDto from(Review review){

        Duration duration = Duration.between(review.getCreatedAt(), LocalDateTime.now());

        long createdTimeAgo = duration.toHours();

        return ReviewResDto.builder()
                .reviewId(review.getReviewId())
                .userId(review.getUser().getId())
                .bookId(review.getBook().getId())
                .nickname(review.getUser().getNickname())
                .profileImageUrl(review.getUser().getProfileImageUrl())
                .reviewColor(review.getReviewColor())
                .reviewContent(review.getReviewContent())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .createdTimeAgo(createdTimeAgo)
                .build();
    }
}