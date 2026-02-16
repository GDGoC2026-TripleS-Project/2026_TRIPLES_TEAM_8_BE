package com.example.gread.app.home.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class OnboardingResponseDto {
    private String nickname;
    private String readerType;       // TYPE_A
    private String testResultTitle;  // 지적인 탐구자
    private String testResultContent;// 예: "당신은 논리적인 추론을 즐기는 독자군요!"
    private String typeImageUrl;     // 결과 이미지 URL
    private List<String> recommendCategories; // 추천 카테고리 (예: ["1", "2"])
    private String imageUrl;
}