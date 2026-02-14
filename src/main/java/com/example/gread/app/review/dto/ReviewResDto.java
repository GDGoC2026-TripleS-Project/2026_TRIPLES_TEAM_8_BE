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
    private Long profileId;
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

        LocalDateTime createdAt = review.getCreatedAt();

        long createdTimeAgo = 0;

        if (createdAt != null) {
                createdTimeAgo = Duration.between(createdAt, LocalDateTime.now()).toHours();
        }

        return ReviewResDto.builder()
                .reviewId(review.getReviewId())
                .profileId(review.getProfile().getId())
                .userId(review.getProfile().getId())
                .bookId(review.getBook().getId())
                .nickname(review.getProfile().getNickname())
                .profileImageUrl(review.getProfile().getProfileImageUrl())
                .reviewColor(review.getReviewColor())
                .reviewContent(review.getReviewContent())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .createdTimeAgo(createdTimeAgo)
                .build();
    }
}