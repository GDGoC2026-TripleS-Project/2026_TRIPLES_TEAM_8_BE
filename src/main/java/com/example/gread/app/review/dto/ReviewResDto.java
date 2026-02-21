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
    private Long bookId;

    private String nickname;
    private ReviewColor reviewColor;
    private String reviewContent;
    private String category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private long createdTimeAgo;

    private String minorCode;

    public static ReviewResDto from(Review review){

        LocalDateTime createdAt = review.getCreatedAt();

        long createdTimeAgo = 0;

        if (createdAt != null) {
            createdTimeAgo = Duration.between(createdAt, LocalDateTime.now()).toHours();
        }

        return ReviewResDto.builder()
                .reviewId(review.getReviewId())
                .profileId(review.getProfile().getId())
                .bookId(review.getBook().getId())
                .nickname(review.getProfile().getNickname())
                .reviewColor(review.getReviewColor())
                .reviewContent(review.getReviewContent())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .createdTimeAgo(createdTimeAgo)
                .minorCode(review.getBook().getMinorCode())
                .build();
    }
}