package com.example.gread.app.login.dto;

import lombok.Getter;

@Getter
public class OnboardingRequestDto {
    private String nickname;
    private String testResultCode; // 프론트에서 계산해서 보내줄 8가지 유형 코드
    private String preferenceTags; // 선택 키워드 2개 (예: "태그1,태그2")
}