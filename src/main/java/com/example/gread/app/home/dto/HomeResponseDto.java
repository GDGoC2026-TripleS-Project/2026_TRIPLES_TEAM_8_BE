package com.example.gread.app.home.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HomeResponseDto {
    private String nickname;
    private String readerType;
    private List<ReviewResponseDto> reviews;

    // 비회원용 정적 팩토리 메서드
    public static HomeResponseDto forGuest(List<ReviewResponseDto> reviews) {
        return HomeResponseDto.builder()
                .nickname("방문자")
                .readerType(null)
                .reviews(reviews)
                .build();
    }
}