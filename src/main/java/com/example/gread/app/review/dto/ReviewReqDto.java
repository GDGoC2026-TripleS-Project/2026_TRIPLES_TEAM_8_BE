package com.example.gread.app.review.dto;

import com.example.gread.app.review.domain.ReviewColor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewReqDto {

    @NotNull(message = "reviewColor은 필수입니다.")
    private ReviewColor reviewColor;

    @NotBlank(message = "reviewContent는 필수입니다.")
    private String reviewContent;
}
