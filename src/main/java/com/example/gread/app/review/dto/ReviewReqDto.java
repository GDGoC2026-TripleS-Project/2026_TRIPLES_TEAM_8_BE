package com.example.gread.app.review.dto;

import com.example.gread.app.review.domain.ReviewColor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewReqDto {

    @NotNull(message = "reviewColor은 필수입니다.")
    private ReviewColor reviewColor;

    @NotBlank(message = "reviewContent는 필수입니다.")
    @Size(max = 100, message = "리뷰 내용은 100자 이내여야 합니다.")
    private String reviewContent;
}