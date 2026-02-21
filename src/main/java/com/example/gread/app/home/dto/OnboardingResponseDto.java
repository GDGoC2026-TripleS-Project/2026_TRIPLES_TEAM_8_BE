package com.example.gread.app.home.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class OnboardingResponseDto {
    private String readerType;               // "TYPE_B"
    private String readerTitle;              // "사유 감상형 독자"
    private List<String> descriptionLines;   // 문장 단위로 쪼개진 리스트
    private String recommendedCategoryCode;
}