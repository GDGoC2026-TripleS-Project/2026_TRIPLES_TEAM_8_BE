package com.example.gread.app.review.dto;

import com.example.gread.app.review.domain.Review;
import com.example.gread.app.review.domain.ReviewColor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class ReviewResDto {

    private Long reviewId;
    private Long bookId;

    private String nickname;
    private ReviewColor reviewColor;
    private String reviewContent;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ReviewResDto from(Review review){
        return ReviewResDto.builder()
                .reviewId(review.getReviewId())
                .bookId(review.getBook().getId())
                .nickname(review.getUser().getNickname())
                .reviewColor(review.getReviewColor())
                .reviewContent(review.getReviewContent())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }
}
